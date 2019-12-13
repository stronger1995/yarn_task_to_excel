package yarn;

import exception.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pojo.TaskRecordDto;
import utils.MonitorConstants;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestforTask {
    public static void main(String[] args) throws IOException {
        // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
//        String pathname = "C:\\Users\\admin\\Desktop\\yarntask20190903.txt";
        String fileName = "yarn_task_20191209";
        String pathname = "C:\\Users\\admin\\Desktop\\" + fileName + ".txt";
        StringBuffer stringBuffer = new StringBuffer();
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
//                System.out.println(line);
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = "E:/";
        LocalDate now = LocalDate.now();
//        String fileName = "demo";
        String fileType = "xlsx";
        String[] taskList = stringBuffer.toString().split("],");
        List<TaskRecordDto> list = new ArrayList<>();
        for (String task : taskList) {
            String[] taskRecords = task.split("\",\"");
            TaskRecordDto taskRecord = new TaskRecordDto();
            Pattern pattern = Pattern.compile(MonitorConstants.REGEX_YARN_TASK_APPID);
            Matcher matcher = pattern.matcher(taskRecords[0]);
            while (matcher.find()) {
                taskRecord.setAppId(matcher.group(1));
            }
            taskRecord.setAppName(taskRecords[2].replaceAll("\"", ""));
            taskRecord.setState(taskRecords[7].replaceAll("\"", ""));
            taskRecord.setStartTime(taskRecords[5].replaceAll("\"", ""));
            taskRecord.setFinishTime(taskRecords[6].replaceAll("\"", ""));
            taskRecord.setContainers(taskRecords[9].replaceAll("\"", ""));
            taskRecord.setCores(taskRecords[10].replaceAll("\"", ""));
            taskRecord.setMems(taskRecords[11].replaceAll("\"", ""));
            taskRecord.setExecUser(taskRecords[1].replaceAll("\"", ""));
            taskRecord.setAppType(taskRecords[3].replaceAll("\"", ""));
            taskRecord.setQueue(taskRecords[4].replaceAll("\"", ""));
            taskRecord.setFinalStatus(taskRecords[8].replaceAll("\"", ""));
            taskRecord.setReservedCores(taskRecords[12].replaceAll("\"", ""));
            taskRecord.setReservedMems(taskRecords[13].replaceAll("\"", ""));
//            taskRecord.setTracking(taskRecords[15].replaceAll("\"", ""));
            Matcher matcherTrack = pattern.matcher(taskRecords[15]);
            while (matcherTrack.find()) {
                taskRecord.setTracking(matcherTrack.group(1));
            }
            list.add(taskRecord);
        }

        String[] title = {"app_id", "user", "app_name", "app_type", "queue", "开始时间", "结束时间", "耗时(分钟)", "状态", "最终状态", "Tracking UI"};

        writer(path, fileName, fileType, list, title);
    }

    @SuppressWarnings("resource")
    public static void writer(String path, String fileName, String fileType, List<TaskRecordDto> list, String[] titleRow) throws IOException {
        Workbook wb = null;
        String excelPath = path + File.separator + fileName + "." + fileType;
        File file = new File(excelPath);
        Sheet sheet = null;
        //创建工作文档对象
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook();

        } else if (fileType.equals("xlsx")) {
            wb = new XSSFWorkbook();

        } else {
            throw new BusinessException("文件格式不正确");
        }
        //创建sheet对象
        sheet = (Sheet) wb.createSheet("sheet1");

        //添加表头
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        row.setHeight((short) 540);
        cell.setCellValue("yarn任务一览");    //创建第一行

        CellStyle style = wb.createCellStyle(); // 样式对象
        // 设置单元格的背景颜色为淡蓝色
        style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);

        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
        style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
        style.setWrapText(true);// 指定当单元格内容显示不下时自动换行

        cell.setCellStyle(style); // 样式，居中

        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 280);
        style.setFont(font);
        // 单元格合并
        // 四个参数分别是：起始行，起始列，结束行，结束列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
        sheet.autoSizeColumn(5200);

        row = sheet.createRow(1);    //创建第二行
        for (int i = 0; i < titleRow.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(titleRow[i]);
            cell.setCellStyle(style); // 样式，居中
            sheet.setColumnWidth(i, 20 * 256);
        }
        row.setHeight((short) 540);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //循环写入行数据
        for (int i = 0; i < list.size(); i++) {
            row = (Row) sheet.createRow(i + 2);
            row.setHeight((short) 500);
            row.createCell(0).setCellValue((list.get(i)).getAppId());
            row.createCell(1).setCellValue((list.get(i)).getExecUser());
            row.createCell(2).setCellValue((list.get(i)).getAppName());
            row.createCell(3).setCellValue((list.get(i)).getAppType());
            row.createCell(4).setCellValue((list.get(i)).getQueue());
            Long startL = Long.valueOf((list.get(i)).getStartTime());
            Long endL = Long.valueOf((list.get(i)).getFinishTime());
            String start = sdf.format(new Date(startL));
            String end = sdf.format(new Date(endL));
            row.createCell(5).setCellValue(start);
            row.createCell(6).setCellValue(end);
            row.createCell(7).setCellValue((endL - startL) / 1000 / 60);
            row.createCell(8).setCellValue((list.get(i)).getState());
            row.createCell(9).setCellValue((list.get(i)).getFinalStatus());
            row.createCell(10).setCellValue((list.get(i)).getTracking());
        }

        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
    }
}

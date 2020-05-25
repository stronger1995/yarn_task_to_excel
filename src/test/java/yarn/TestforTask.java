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
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestforTask {
    public static void main(String[] args) throws IOException {
        // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
//        String pathname = "C:\\Users\\admin\\Desktop\\yarntask20190903.txt";
        String fileName = "yarn_task_20200512";
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

        // 写入excel
        writer(path, fileName, fileType, list, title);
        // 计算每小时任务数
        calculate(list);
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

    /**
     * 统计每小时任务数
     */
    public static void calculate(List<TaskRecordDto> taskList) {
        List<LocalDate> localDates = getLocalDates();
        Map<LocalDate, List<Long>> dateMap = getDateMap();

        // 统计每天每小时任务数
        for (LocalDate date : localDates) {
            List<Long> hourList = dateMap.get(date);
            for (int i = 0; i < hourList.size() - 1; i++) {
                int count = 0;
                for (TaskRecordDto task : taskList) {
                    String startTime = task.getStartTime();
                    long start = Long.parseLong(startTime);
                    // hour1 <= start < hour2
                    if (start >= hourList.get(i) && start < hourList.get(i + 1)) {
                        count++;
                    }
                }
                System.out.println(String.format("时间\t%s\t数量\t%d", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(hourList.get(i))), count));
            }
        }


    }

    public static Map<LocalDate, List<Long>> getDateMap() {
        List<LocalDate> dateList = getLocalDates();
        Map<LocalDate, List<Long>> dateMap = new HashMap<>();
        for (LocalDate localDate : dateList) {
            List<Long> hourList = new ArrayList<>();
            // 每天24小时
            long hour00 = localDate.atTime(0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour01 = localDate.atTime(1, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour02 = localDate.atTime(2, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour03 = localDate.atTime(3, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour04 = localDate.atTime(4, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour05 = localDate.atTime(5, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour06 = localDate.atTime(6, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour07 = localDate.atTime(7, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour08 = localDate.atTime(8, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour09 = localDate.atTime(9, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour10 = localDate.atTime(10, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour11 = localDate.atTime(11, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour12 = localDate.atTime(12, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour13 = localDate.atTime(13, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour14 = localDate.atTime(14, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour15 = localDate.atTime(15, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour16 = localDate.atTime(16, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour17 = localDate.atTime(17, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour18 = localDate.atTime(18, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour19 = localDate.atTime(19, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour20 = localDate.atTime(20, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour21 = localDate.atTime(21, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour22 = localDate.atTime(22, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long hour23 = localDate.atTime(23, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            long nextDayHour00 = localDate.plusDays(1).atTime(0, 0, 0).toInstant(ZoneOffset.of("+8")).toEpochMilli();
            hourList.add(hour00);
            hourList.add(hour01);
            hourList.add(hour02);
            hourList.add(hour03);
            hourList.add(hour04);
            hourList.add(hour05);
            hourList.add(hour06);
            hourList.add(hour07);
            hourList.add(hour08);
            hourList.add(hour09);
            hourList.add(hour10);
            hourList.add(hour11);
            hourList.add(hour12);
            hourList.add(hour13);
            hourList.add(hour14);
            hourList.add(hour15);
            hourList.add(hour16);
            hourList.add(hour17);
            hourList.add(hour18);
            hourList.add(hour19);
            hourList.add(hour20);
            hourList.add(hour21);
            hourList.add(hour22);
            hourList.add(hour23);
            hourList.add(nextDayHour00);

            dateMap.put(localDate, hourList);
        }
        return dateMap;
    }

    private static List<LocalDate> getLocalDates() {
        // 过去一个星期
        LocalDate day01 = LocalDate.of(2020, 5, 4);
        LocalDate day02 = LocalDate.of(2020, 5, 5);
        LocalDate day03 = LocalDate.of(2020, 5, 6);
        LocalDate day04 = LocalDate.of(2020, 5, 7);
        LocalDate day05 = LocalDate.of(2020, 5, 8);
        LocalDate day06 = LocalDate.of(2020, 5, 9);
        LocalDate day07 = LocalDate.of(2020, 5, 10);
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(day01);
        dateList.add(day02);
        dateList.add(day03);
        dateList.add(day04);
        dateList.add(day05);
        dateList.add(day06);
        dateList.add(day07);
        return dateList;
    }
}


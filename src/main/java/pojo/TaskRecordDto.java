package pojo;

import lombok.Data;
import utils.MonitorConstants;

/**
 * 每日任务一览列表数据
 */
@Data
public class TaskRecordDto implements Comparable<TaskRecordDto> {
    private String appId;
    private String appName;
    private String state;
    private Byte runCycle;
    private String startTime;
    private String finishTime;
    private Integer taskOwnerId;
    private String fixedNode;
    private String containers;
    private String cores;
    private String mems;
    private String execUser;
    private String appType;
    private String queue;
    private String finalStatus;
    private String reservedCores;
    private String reservedMems;
    private Byte startType;
    private Integer taskId;
    private Byte deployFlag;
    private String tracking;

    @Override
    public int compareTo(TaskRecordDto o) {
        // 排序规则：
        // 1.状态字段 RUNNING > FINISHED > KILLED > FAILED > READY
        // 2.app名称 desc
        // 3.appId desc
        if (null == o) return 0;
        if (this.getState().equals(o.getState())) {
            if (this.getAppName().equals(o.getAppName())) {
                if (null == o.getAppId()) return 0;
                return o.getAppId().compareTo(this.getAppId());
            } else {
                return o.getAppName().compareTo(this.getAppName());
            }
        } else {
            String state1 = this.getState();
            String state2 = o.getState();
            state1 = getSortState(state1);
            state2 = getSortState(state2);
            return state1.compareTo(state2);
        }
    }

    private String getSortState(String state) {
        if (MonitorConstants.YARN_TASK_STATE_RUNNING.equals(state)) {
            state = "a";
        }
        if (MonitorConstants.YARN_TASK_STATE_FINISHED.equals(state)) {
            state = "b";
        }
        if (MonitorConstants.YARN_TASK_STATE_KILLED.equals(state)) {
            state = "c";
        }
        if (MonitorConstants.YARN_TASK_STATE_FAILED.equals(state)) {
            state = "d";
        }
        if (MonitorConstants.YARN_TASK_STATE_READY.equals(state)) {
            state = "e";
        }
        return state;
    }
}

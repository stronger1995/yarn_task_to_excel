package utils;

/**
 * 常量类
 */
public interface MonitorConstants {
    /**
     * yarn任务app_id匹配正则表达式
     */
    String REGEX_YARN_TASK_APPID = ">([\\w]*)</a>";

    /**
     * yarn任务状态: RUNNING
     */
    String YARN_TASK_STATE_RUNNING = "RUNNING";

    /**
     * yarn任务状态: READY
     */
    String YARN_TASK_STATE_READY = "READY";

    /**
     * yarn任务状态: FINISHED
     */
    String YARN_TASK_STATE_FINISHED = "FINISHED";

    /**
     * yarn任务状态: FAILED
     */
    String YARN_TASK_STATE_FAILED = "FAILED";

    /**
     * yarn任务状态: KILLED
     */
    String YARN_TASK_STATE_KILLED = "KILLED";

    /**
     * 任务启动周期: BYDAY
     */
    Byte TASK_RUNCYCLE_BYDAY = 2;

    /**
     * 任务是否立即启动: 1 是
     */
    Byte TASK_STARTNOWFLAG_YES = 1;

    /**
     * 任务启动类型: 1 本平台
     */
    Byte TASK_START_TYPE_LOCAL = 1;

    /**
     * 任务部署标志: 1 已部署
     */
    Byte TASK_DEPLOY_FLAG_FINISH = 1;

    /**
     * 废弃时间戳: 默认值 99991231235959.999
     */
    String TASK_DISCARD_TIME_DEFAULT = "99991231235959.999";
}

//******************************************************************************
// 版权所有(c)，科大国创，保留所有权利。
//******************************************************************************

package pojo;

/**
 *
 * 本类自动生成，对应的表为[tb_task_today].
 */
@lombok.Getter
@lombok.Setter
@lombok.ToString
public class TbTaskToday {
    /**
     * 主键ID (tb_task_today.id).
     */
    private Integer id;

    /**
     * 任务id (tb_task_today.app_id).
     */
    private String appId;

    /**
     * 任务名称 (tb_task_today.app_name).
     */
    private String appName;

    /**
     * 任务状态 (tb_task_today.state).
     */
    private String state;

    /**
     * 执行周期(1:ALWAYS;2:BYDAY;3:OTHER) (tb_task_today.run_cycle).
     */
    private Byte runCycle;

    /**
     * 开始时间 (tb_task_today.start_time).
     */
    private String startTime;

    /**
     * 结束时间 (tb_task_today.finish_time).
     */
    private String finishTime;

    /**
     * 任务所有者ID (tb_task_today.task_owner_id).
     */
    private Integer taskOwnerId;

    /**
     * 启动节点名 (tb_task_today.fixed_node).
     */
    private String fixedNode;

    /**
     * 容器数量 (tb_task_today.containers).
     */
    private String containers;

    /**
     * 已分配CPU核数 (tb_task_today.cores).
     */
    private String cores;

    /**
     * 已分配内存 (tb_task_today.mems).
     */
    private String mems;

    /**
     * 执行用户 (tb_task_today.exec_user).
     */
    private String execUser;

    /**
     * APP类型 (tb_task_today.app_type).
     */
    private String appType;

    /**
     * yarn队列 (tb_task_today.queue).
     */
    private String queue;

    /**
     * 最终状态 (tb_task_today.final_status).
     */
    private String finalStatus;

    /**
     * 保留CPU核数 (tb_task_today.reserved_cores).
     */
    private String reservedCores;

    /**
     * 保留内存 (tb_task_today.reserved_mems).
     */
    private String reservedMems;

    /**
     * 启动类型(1:本平台;9:其他平台) (tb_task_today.start_type).
     */
    private Byte startType;

    /**
     * 任务ID (tb_task_today.task_id).
     */
    private Integer taskId;

    /**
     * 部署标志(0:未部署;1:已部署) (tb_task_today.deploy_flag).
     */
    private Byte deployFlag;
}
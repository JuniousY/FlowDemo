package org.dev.flowdemo.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TaskConstants {

    @AllArgsConstructor
    @Getter
    public enum ProjectStatus {
        DELETED(-1, "已删除"),
        NOT_STARTED(0, "未开始"),
        IN_PROGRESS(1, "进行中"),
        COMPLETED(2, "已完成");

        private final int code;
        private final String description;
    }

    @AllArgsConstructor
    @Getter
    public enum TaskStatus {
        NOT_STARTED(0, "未开始"),
        RESEARCH(1, "调研阶段"),
        INITIATION(2, "立项阶段"),
        DESIGN(3, "设计阶段"),
        VERIFICATION(4, "验证阶段"),
        COMPLETED(5, "已完成"),
        DELETED(-1, "已删除");

        private final int code;
        private final String description;

        public static TaskStatus getInstance(int code) {
            for (TaskStatus status : TaskStatus.values()) {
                if (status.getCode() == code) {
                    return status;
                }
            }
            return TaskStatus.NOT_STARTED;
        }
    }
}

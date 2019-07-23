package coordinator.models.transitions;

public enum State {
    //Initial State
    MONITORING_NOT_STARTED,
    
    //Validation Phase
    WAITING_VALIDATION,
    DOING_VALIDATION,
    FINISHED_VALIDATION,
    ERROR_VALIDATION,
    
    // Balance Phase
    WAITING_BALANCE,
    DOING_BALANCE,
    FINISHED_BALANCE,
    ERROR_BALANCE,
    WAITING_UNDOING_BALANCE,
    UNDOING_BALANCE,
    FINISHED_UNDOING_BALANCE,

    // Signaling Phase
    WAITING_SIGNALING,
    DOING_SIGNALING,
    FINISHED_SIGNALING,
    ERROR_SIGNALING,
    WAITING_UNDOING_SIGNALING,
    UNDOING_SIGNALING,
    FINISHED_UNDOING_SIGNALING,

    // Receipt Phase
    WAITING_RECEIPT,
    DOING_RECEIPT,
    FINISHED_RECEIPT,
    ERROR_RECEIPT,

    //Final States
    TRANSFER_FAILED,
    FAILED_VALIDATION,

    //Helpers
    UNDO_ALL,
    UNDO_ALL_FINISHED,
    PARALLEL_TASKS,
    JOIN_UNDO,
    JOIN_PARALLEL_TASKS,
    PARALLEL_TASKS_UNDO
}
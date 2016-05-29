package models;

public enum JobRequestType {
    COLLABORATIVE_FILTERING;

    public static int parameterSize(JobRequestType jobRequestType) {
        int size = 0;

        switch (jobRequestType) {
            case COLLABORATIVE_FILTERING:
                size = 3;
                break;
        }

        return size;
    }
}

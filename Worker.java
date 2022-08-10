public interface Worker {
    void work(Book book);
    void getFree();
    void helpTheChild(String parent);
    boolean getIsWorking();
    void setIsWorking(boolean isWorking);
}

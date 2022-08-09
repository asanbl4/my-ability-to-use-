public interface Worker {
    void work(Book book);
    void getFree();
    void helpSomebody();
    boolean getIsWorking();
    void setIsWorking(boolean isWorking);
}

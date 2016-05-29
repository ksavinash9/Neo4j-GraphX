package models;

import java.io.BufferedReader;

public abstract class ParallelReaderConfiguration<T> {
    int start;
    int length;
    BufferedReader bufferedReader;
    Integer fullSize;
    Integer reportBlockSize;

    public ParallelReaderConfiguration(int start, int length, BufferedReader bufferedReader, Integer fullSize, Integer reportBlockSize) {
        this.start = start;
        this.length = length;
        this.bufferedReader = bufferedReader;
        this.fullSize = fullSize;
        this.reportBlockSize = reportBlockSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public void setBufferedReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public Integer getFullSize() {
        return fullSize;
    }

    public void setFullSize(Integer fullSize) {
        this.fullSize = fullSize;
    }

    public Integer getReportBlockSize() {
        return reportBlockSize;
    }

    public void setReportBlockSize(Integer reportBlockSize) {
        this.reportBlockSize = reportBlockSize;
    }

    public void transactBlock(T block) {

    }

    public ParallelReaderConfiguration<T> clone() throws CloneNotSupportedException {
        return (ParallelReaderConfiguration<T>)super.clone();
    }
}

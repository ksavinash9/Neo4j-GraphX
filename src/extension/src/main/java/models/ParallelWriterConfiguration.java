package models;

import java.io.BufferedWriter;
import java.io.IOException;

public abstract class ParallelWriterConfiguration<T> {
    int start;
    int length;
    BufferedWriter bufferedWriter;
    Integer fullSize;
    Integer reportBlockSize;

    public ParallelWriterConfiguration(int start, int length, BufferedWriter bufferedWriter, Integer fullSize, Integer reportBlockSize) {
        this.start = start;
        this.length = length;
        this.bufferedWriter = bufferedWriter;
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

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public void setBufferedWriter(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
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

    public void writeBlock(T block) {
        try {
            bufferedWriter.write(block.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ParallelWriterConfiguration<T> clone() throws CloneNotSupportedException {
        return (ParallelWriterConfiguration<T>)super.clone();
    }
}

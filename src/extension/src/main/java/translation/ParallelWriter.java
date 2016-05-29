package translation;

import models.ParallelWriterConfiguration;

import java.io.IOException;
import java.util.Spliterator;
import java.util.concurrent.RecursiveAction;

public class ParallelWriter<T> extends RecursiveAction {
    private static final int THREAD_COUNT = 4;
    private Spliterator<T>[] src;
    private ParallelWriterConfiguration<T> configuration;

    public ParallelWriter(Spliterator<T>[] src, ParallelWriterConfiguration<T> configuration) {
        this.src = src;
        this.configuration = configuration;
    }

    @Override
    protected void compute() {
        if (configuration.getLength() <= (configuration.getFullSize() / THREAD_COUNT)) {
            try {
                computeDirectly();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        int split = configuration.getLength() / 2;

        try {
            ParallelWriterConfiguration<T> split1 = configuration.clone();
            ParallelWriterConfiguration<T> split2 = configuration.clone();
            split1.setLength(split);
            split2.setStart(split2.getStart() + split);
            split2.setLength(split2.getLength() - split);
            invokeAll(new ParallelWriter<>(src, split1), new ParallelWriter<>(src, split2));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes a section of the source array and applies updates to those nodes referenced
     * in each block.
     * @throws IOException
     */
    protected void computeDirectly() throws IOException {
        for(int i = configuration.getStart(); i < configuration.getStart() + configuration.getLength(); i++) {
            src[i].forEachRemaining(configuration::writeBlock);
        }
    }


}

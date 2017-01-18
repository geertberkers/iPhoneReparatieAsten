package geert.berkers.iphonereparatieasten.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import geert.berkers.iphonereparatieasten.R;
import geert.berkers.iphonereparatieasten.model.TestItem;
import geert.berkers.iphonereparatieasten.views.TestItemViewHolder;

/**
 * Created by Geert.
 */
public class TestItemAdapter extends RecyclerView.Adapter<TestItemViewHolder> {

    private final List<TestItem> testItems;

    public TestItemAdapter(List<TestItem> testItems) {
        this.testItems = testItems;
    }

    @Override
    public TestItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.testitem_cardview, parent, false);
        return new TestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TestItemViewHolder holder, int position) {
        TestItem testItem = testItems.get(position);

        //TODO: Multiple testresults in 1 testitem!
        int imageResource;
        switch(testItem.getTestResult()){
            case FAILED:        imageResource = testItem.getFailedImageResource();      break;
            case PASSED:        imageResource = testItem.getPassedImageResource();      break;
            case NO_PERMISSION: imageResource = testItem.getFailedImageResource();      break;
            case NOT_TESTED:
            default:            imageResource = testItem.getUntestedImageResource();    break;
        }

        holder.testName.setText(testItem.getTestName());
        holder.testImage.setImageResource(imageResource);
    }

    @Override
    public int getItemCount() {
        return testItems.size();
    }
}

package ggn.ameba.post.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by gagandeep on 22 Apr 2016.
 */

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener
{
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private int     previousTotal            = 0; // The total number of items in the dataset after the last load
    private boolean loading                  = true; // True if we are still waiting for the last set of data to load.
    private int     visibleThreshold         = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private StaggeredGridLayoutManager mLinearLayoutManager;
    private LinearLayoutManager        mLinearLayoutManagerG;

    public EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager linearLayoutManager)
    {
        this.mLinearLayoutManager = linearLayoutManager;
    }


    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager)
    {
        this.mLinearLayoutManagerG = linearLayoutManager;
        mLinearLayoutManager = null;
    }


    public void startOverStaggered()
    {
        current_page = 1;
        previousTotal = 0;
        loading=true;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);

        if (mLinearLayoutManager != null)
        {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPositions(null)[0];

            if (loading)
            {
                if (totalItemCount > previousTotal)
                {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold))
            {
                // End has been reached

                // Do something
                current_page++;

                onLoadMore(current_page);

                loading = true;
            }
        }
        else
        {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManagerG.getItemCount();
            firstVisibleItem = mLinearLayoutManagerG.findFirstVisibleItemPosition();

            if (loading)
            {
                if (totalItemCount > previousTotal)
                {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold))
            {
                // End has been reached

                // Do something
                current_page++;

                onLoadMore(current_page);

                loading = true;
            }
        }


    }

    public abstract void onLoadMore(int current_page);
}
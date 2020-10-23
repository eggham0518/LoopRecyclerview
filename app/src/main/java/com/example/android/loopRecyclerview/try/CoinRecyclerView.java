import android.content.Context;
import android.database.Observable;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.os.TraceCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.NestedScrollingChild2;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//public class CoinRecyclerView extends ViewGroup implements ScrollingView,
//        NestedScrollingChild2, NestedScrollingChild3 {
//    static final String TAG = "CoinRecyclerView";
//    static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
//    static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
//
//    public static final int NO_POSITION = -1;
//    public static final long NO_ID = -1;
//    public static final int INVALID_TYPE = -1;
//
//    static final boolean DEBUG = false;
//
//    static final boolean VERBOSE_TRACING = false;
//
//    private static final int[]  NESTED_SCROLLING_ATTRS =
//            {16843830 /* android.R.attr.nestedScrollingEnabled */};
//
//    static final boolean FORCE_INVALIDATE_DISPLAY_LIST = Build.VERSION.SDK_INT == 18
//            || Build.VERSION.SDK_INT == 19 || Build.VERSION.SDK_INT == 20;
//
//    static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC = Build.VERSION.SDK_INT >= 23;
//
//    static final boolean POST_UPDATES_ON_ANIMATION = Build.VERSION.SDK_INT >= 16;
//
//    static final boolean ALLOW_THREAD_GAP_WORK = Build.VERSION.SDK_INT >= 21;
//
//    private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION = Build.VERSION.SDK_INT <= 15;
//
//    private static final boolean IGNORE_DETACHED_FOCUSED_CHILD = Build.VERSION.SDK_INT <= 15;
//
//    static final boolean DISPATCH_TEMP_DETACH = false;
//
//    AdapterHelper adapterHelper;
//
//    public CoinRecyclerView(Context context) {
//        super(context);
//    }
//
//
//    public abstract static class Adapter<VH extends CoinRecyclerView.ViewHolder> {
//        private final CoinRecyclerView.AdapterDataObservable mObservable = new CoinRecyclerView.AdapterDataObservable();
//        private boolean mHasStableIds = false;
//
//        @NonNull
//        public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
//
//        public abstract void onBindViewHolder(@NonNull VH holder, int position);
//
//        public void onBindViewHolder(@NonNull VH holder, int position,
//                                     @NonNull List<Object> payloads) {
//            onBindViewHolder(holder, position);
//        }
//
//        @NonNull
//        public final VH createViewHolder(@NonNull ViewGroup parent, int viewType) {
//            try {
//                TraceCompat.beginSection(TRACE_CREATE_VIEW_TAG);
//                final VH holder = onCreateViewHolder(parent, viewType);
//                if (holder.itemView.getParent() != null) {
//                    throw new IllegalStateException("ViewHolder views must not be attached when"
//                            + " created. Ensure that you are not passing 'true' to the attachToRoot"
//                            + " parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
//                }
//                holder.mItemViewType = viewType;
//                return holder;
//            } finally {
//                TraceCompat.endSection();
//            }
//        }
//
//        public final void bindViewHolder(@NonNull VH holder, int position) {
//            holder.mPosition = position;
//            if (hasStableIds()) {
//                holder.mItemId = getItemId(position);
//            }
//            holder.setFlags(ViewHolder.FLAG_BOUND,
//                    ViewHolder.FLAG_BOUND | ViewHolder.FLAG_UPDATE | ViewHolder.FLAG_INVALID
//                            | ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN);
//            TraceCompat.beginSection(TRACE_BIND_VIEW_TAG);
//            onBindViewHolder(holder, position, holder.getUnmodifiedPayloads());
//            holder.clearPayload();
//            final ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//            if (layoutParams instanceof CoinRecyclerView.LayoutParams) {
//                ((CoinRecyclerView.LayoutParams) layoutParams).mInsetsDirty = true;
//            }
//            TraceCompat.endSection();
//        }
//
//        public int getItemViewType(int position) {
//            return 0;
//        }
//
//        public void setHasStableIds(boolean hasStableIds) {
//            if (hasObservers()) {
//                throw new IllegalStateException("Cannot change whether this adapter has "
//                        + "stable IDs while the adapter has registered observers.");
//            }
//            mHasStableIds = hasStableIds;
//        }
//
//        public long getItemId(int position) {
//            return NO_ID;
//        }
//
//        public abstract int getItemCount();
//
//        public final boolean hasStableIds() {
//            return mHasStableIds;
//        }
//
//        public void onViewRecycled(@NonNull VH holder) {
//        }
//
//        public boolean onFailedToRecycleView(@NonNull VH holder) {
//            return false;
//        }
//
//        public void onViewAttachedToWindow(@NonNull VH holder) {
//        }
//
//        public void onViewDetachedFromWindow(@NonNull VH holder) {
//        }
//
//        public final boolean hasObservers() {
//            return mObservable.hasObservers();
//        }
//
//        public void registerAdapterDataObserver(@NonNull AdapterDataObserver observer) {
//            mObservable.registerObserver(observer);
//        }
//
//        public void unregisterAdapterDataObserver(@NonNull AdapterDataObserver observer) {
//            mObservable.unregisterObserver(observer);
//        }
//
//        public void onAttachedToRecyclerView(@NonNull CoinRecyclerView CoinRecyclerView) {
//        }
//
//        public void onDetachedFromRecyclerView(@NonNull CoinRecyclerView CoinRecyclerView) {
//        }
//
//        public final void notifyDataSetChanged() {
//            mObservable.notifyChanged();
//        }
//
//        public final void notifyItemChanged(int position) {
//            mObservable.notifyItemRangeChanged(position, 1);
//        }
//
//        public final void notifyItemChanged(int position, @Nullable Object payload) {
//            mObservable.notifyItemRangeChanged(position, 1, payload);
//        }
//
//        public final void notifyItemRangeChanged(int positionStart, int itemCount) {
//            mObservable.notifyItemRangeChanged(positionStart, itemCount);
//        }
//
//        public final void notifyItemRangeChanged(int positionStart, int itemCount,
//                                                 @Nullable Object payload) {
//            mObservable.notifyItemRangeChanged(positionStart, itemCount, payload);
//        }
//
//        public final void notifyItemInserted(int position) {
//            mObservable.notifyItemRangeInserted(position, 1);
//        }
//
//        public final void notifyItemMoved(int fromPosition, int toPosition) {
//            mObservable.notifyItemMoved(fromPosition, toPosition);
//        }
//
//        public final void notifyItemRangeInserted(int positionStart, int itemCount) {
//            mObservable.notifyItemRangeInserted(positionStart, itemCount);
//        }
//
//        public final void notifyItemRemoved(int position) {
//            mObservable.notifyItemRangeRemoved(position, 1);
//        }
//
//        public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
//            mObservable.notifyItemRangeRemoved(positionStart, itemCount);
//        }
//    }
//
//    public abstract static class ViewHolder {
//        @NonNull
//        public final View itemView;
//        WeakReference<CoinRecyclerView> mNestedRecyclerView;
//        int mPosition = NO_POSITION;
//        int mOldPosition = NO_POSITION;
//        long mItemId = NO_ID;
//        int mItemViewType = INVALID_TYPE;
//        int mPreLayoutPosition = NO_POSITION;
//
//        // The item that this holder is shadowing during an item change event/animation
//        CoinRecyclerView.ViewHolder mShadowedHolder = null;
//        // The item that is shadowing this holder during an item change event/animation
//        CoinRecyclerView.ViewHolder mShadowingHolder = null;
//
//        static final int FLAG_BOUND = 1 << 0;
//
//        static final int FLAG_UPDATE = 1 << 1;
//
//        static final int FLAG_INVALID = 1 << 2;
//
//        static final int FLAG_REMOVED = 1 << 3;
//
//        static final int FLAG_NOT_RECYCLABLE = 1 << 4;
//
//        static final int FLAG_RETURNED_FROM_SCRAP = 1 << 5;
//
//        static final int FLAG_IGNORE = 1 << 7;
//
//        static final int FLAG_TMP_DETACHED = 1 << 8;
//
//        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 1 << 9;
//
//        static final int FLAG_ADAPTER_FULLUPDATE = 1 << 10;
//
//        static final int FLAG_MOVED = 1 << 11;
//
//        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 1 << 12;
//
//        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
//
//        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 1 << 13;
//
//        int mFlags;
//
//        private static final List<Object> FULLUPDATE_PAYLOADS = Collections.emptyList();
//
//        List<Object> mPayloads = null;
//        List<Object> mUnmodifiedPayloads = null;
//
//        private int mIsRecyclableCount = 0;
//
//
//        Recycler mScrapContainer = null;
//
//        boolean mInChangeScrap = false;
//
//        private int mWasImportantForAccessibilityBeforeHidden =
//                ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;
//
//        @VisibleForTesting
//        int mPendingAccessibilityState = PENDING_ACCESSIBILITY_STATE_NOT_SET;
//
//        CoinRecyclerView mOwnerRecyclerView;
//
//        public ViewHolder(@NonNull View itemView) {
//            if (itemView == null) {
//                throw new IllegalArgumentException("itemView may not be null");
//            }
//            this.itemView = itemView;
//        }
//
//        void flagRemovedAndOffsetPosition(int mNewPosition, int offset, boolean applyToPreLayout) {
//            addFlags(CoinRecyclerView.ViewHolder.FLAG_REMOVED);
//            offsetPosition(offset, applyToPreLayout);
//            mPosition = mNewPosition;
//        }
//
//        void offsetPosition(int offset, boolean applyToPreLayout) {
//            if (mOldPosition == NO_POSITION) {
//                mOldPosition = mPosition;
//            }
//            if (mPreLayoutPosition == NO_POSITION) {
//                mPreLayoutPosition = mPosition;
//            }
//            if (applyToPreLayout) {
//                mPreLayoutPosition += offset;
//            }
//            mPosition += offset;
//            if (itemView.getLayoutParams() != null) {
//                ((CoinRecyclerView.LayoutParams) itemView.getLayoutParams()).mInsetsDirty = true;
//            }
//        }
//
//        void clearOldPosition() {
//            mOldPosition = NO_POSITION;
//            mPreLayoutPosition = NO_POSITION;
//        }
//
//        void saveOldPosition() {
//            if (mOldPosition == NO_POSITION) {
//                mOldPosition = mPosition;
//            }
//        }
//
//        boolean shouldIgnore() {
//            return (mFlags & FLAG_IGNORE) != 0;
//        }
//
//        @Deprecated
//        public final int getPosition() {
//            return mPreLayoutPosition == NO_POSITION ? mPosition : mPreLayoutPosition;
//        }
//
//        public final int getLayoutPosition() {
//            return mPreLayoutPosition == NO_POSITION ? mPosition : mPreLayoutPosition;
//        }
//
//        public final int getAdapterPosition() {
//            if (mOwnerRecyclerView == null) {
//                return NO_POSITION;
//            }
//            return mOwnerRecyclerView.getAdapterPositionFor(this);
//        }
//
//        public final int getOldPosition() {
//            return mOldPosition;
//        }
//
//        public final long getItemId() {
//            return mItemId;
//        }
//
//        public final int getItemViewType() {
//            return mItemViewType;
//        }
//
//        boolean isScrap() {
//            return mScrapContainer != null;
//        }
//
//        void unScrap() {
//            mScrapContainer.unscrapView(this);
//        }
//
//        boolean wasReturnedFromScrap() {
//            return (mFlags & FLAG_RETURNED_FROM_SCRAP) != 0;
//        }
//
//        void clearReturnedFromScrapFlag() {
//            mFlags = mFlags & ~FLAG_RETURNED_FROM_SCRAP;
//        }
//
//        void clearTmpDetachFlag() {
//            mFlags = mFlags & ~FLAG_TMP_DETACHED;
//        }
//
//        void stopIgnoring() {
//            mFlags = mFlags & ~FLAG_IGNORE;
//        }
//
//        void setScrapContainer(Recycler recycler, boolean isChangeScrap) {
//            mScrapContainer = recycler;
//            mInChangeScrap = isChangeScrap;
//        }
//
//        boolean isInvalid() {
//            return (mFlags & FLAG_INVALID) != 0;
//        }
//
//        boolean needsUpdate() {
//            return (mFlags & FLAG_UPDATE) != 0;
//        }
//
//        boolean isBound() {
//            return (mFlags & FLAG_BOUND) != 0;
//        }
//
//        boolean isRemoved() {
//            return (mFlags & FLAG_REMOVED) != 0;
//        }
//
//        boolean hasAnyOfTheFlags(int flags) {
//            return (mFlags & flags) != 0;
//        }
//
//        boolean isTmpDetached() {
//            return (mFlags & FLAG_TMP_DETACHED) != 0;
//        }
//
//        boolean isAttachedToTransitionOverlay() {
//            return itemView.getParent() != null && itemView.getParent() != mOwnerRecyclerView;
//        }
//
//        boolean isAdapterPositionUnknown() {
//            return (mFlags & FLAG_ADAPTER_POSITION_UNKNOWN) != 0 || isInvalid();
//        }
//
//        void setFlags(int flags, int mask) {
//            mFlags = (mFlags & ~mask) | (flags & mask);
//        }
//
//        void addFlags(int flags) {
//            mFlags |= flags;
//        }
//
//        void addChangePayload(Object payload) {
//            if (payload == null) {
//                addFlags(FLAG_ADAPTER_FULLUPDATE);
//            } else if ((mFlags & FLAG_ADAPTER_FULLUPDATE) == 0) {
//                createPayloadsIfNeeded();
//                mPayloads.add(payload);
//            }
//        }
//
//        private void createPayloadsIfNeeded() {
//            if (mPayloads == null) {
//                mPayloads = new ArrayList<Object>();
//                mUnmodifiedPayloads = Collections.unmodifiableList(mPayloads);
//            }
//        }
//
//        void clearPayload() {
//            if (mPayloads != null) {
//                mPayloads.clear();
//            }
//            mFlags = mFlags & ~FLAG_ADAPTER_FULLUPDATE;
//        }
//
//        List<Object> getUnmodifiedPayloads() {
//            if ((mFlags & FLAG_ADAPTER_FULLUPDATE) == 0) {
//                if (mPayloads == null || mPayloads.size() == 0) {
//                    // Initial state,  no update being called.
//                    return FULLUPDATE_PAYLOADS;
//                }
//                // there are none-null payloads
//                return mUnmodifiedPayloads;
//            } else {
//                // a full update has been called.
//                return FULLUPDATE_PAYLOADS;
//            }
//        }
//
//        void resetInternal() {
//            mFlags = 0;
//            mPosition = NO_POSITION;
//            mOldPosition = NO_POSITION;
//            mItemId = NO_ID;
//            mPreLayoutPosition = NO_POSITION;
//            mIsRecyclableCount = 0;
//            mShadowedHolder = null;
//            mShadowingHolder = null;
//            clearPayload();
//            mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;
//            mPendingAccessibilityState = PENDING_ACCESSIBILITY_STATE_NOT_SET;
//            clearNestedRecyclerViewIfNotNested(this);
//        }
//
//        void onEnteredHiddenState(CoinRecyclerView parent) {
//            // While the view item is in hidden state, make it invisible for the accessibility.
//            if (mPendingAccessibilityState != PENDING_ACCESSIBILITY_STATE_NOT_SET) {
//                mWasImportantForAccessibilityBeforeHidden = mPendingAccessibilityState;
//            } else {
//                mWasImportantForAccessibilityBeforeHidden =
//                        ViewCompat.getImportantForAccessibility(itemView);
//            }
//            parent.setChildImportantForAccessibilityInternal(this,
//                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
//        }
//
//        void onLeftHiddenState(CoinRecyclerView parent) {
//            parent.setChildImportantForAccessibilityInternal(this,
//                    mWasImportantForAccessibilityBeforeHidden);
//            mWasImportantForAccessibilityBeforeHidden = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO;
//        }
//
//        @Override
//        public String toString() {
//            String className =
//                    getClass().isAnonymousClass() ? "ViewHolder" : getClass().getSimpleName();
//            final StringBuilder sb = new StringBuilder(className + "{"
//                    + Integer.toHexString(hashCode()) + " position=" + mPosition + " id=" + mItemId
//                    + ", oldPos=" + mOldPosition + ", pLpos:" + mPreLayoutPosition);
//            if (isScrap()) {
//                sb.append(" scrap ")
//                        .append(mInChangeScrap ? "[changeScrap]" : "[attachedScrap]");
//            }
//            if (isInvalid()) sb.append(" invalid");
//            if (!isBound()) sb.append(" unbound");
//            if (needsUpdate()) sb.append(" update");
//            if (isRemoved()) sb.append(" removed");
//            if (shouldIgnore()) sb.append(" ignored");
//            if (isTmpDetached()) sb.append(" tmpDetached");
//            if (!isRecyclable()) sb.append(" not recyclable(" + mIsRecyclableCount + ")");
//            if (isAdapterPositionUnknown()) sb.append(" undefined adapter position");
//
//            if (itemView.getParent() == null) sb.append(" no parent");
//            sb.append("}");
//            return sb.toString();
//        }
//
//        public final void setIsRecyclable(boolean recyclable) {
//            mIsRecyclableCount = recyclable ? mIsRecyclableCount - 1 : mIsRecyclableCount + 1;
//            if (mIsRecyclableCount < 0) {
//                mIsRecyclableCount = 0;
//                if (DEBUG) {
//                    throw new RuntimeException("isRecyclable decremented below 0: "
//                            + "unmatched pair of setIsRecyable() calls for " + this);
//                }
//                Log.e(VIEW_LOG_TAG, "isRecyclable decremented below 0: "
//                        + "unmatched pair of setIsRecyable() calls for " + this);
//            } else if (!recyclable && mIsRecyclableCount == 1) {
//                mFlags |= FLAG_NOT_RECYCLABLE;
//            } else if (recyclable && mIsRecyclableCount == 0) {
//                mFlags &= ~FLAG_NOT_RECYCLABLE;
//            }
//            if (DEBUG) {
//                Log.d(TAG, "setIsRecyclable val:" + recyclable + ":" + this);
//            }
//        }
//
//        public final boolean isRecyclable() {
//            return (mFlags & FLAG_NOT_RECYCLABLE) == 0
//                    && !ViewCompat.hasTransientState(itemView);
//        }
//
//        boolean shouldBeKeptAsChild() {
//            return (mFlags & FLAG_NOT_RECYCLABLE) != 0;
//        }
//
//        boolean doesTransientStatePreventRecycling() {
//            return (mFlags & FLAG_NOT_RECYCLABLE) == 0 && ViewCompat.hasTransientState(itemView);
//        }
//
//        boolean isUpdated() {
//            return (mFlags & FLAG_UPDATE) != 0;
//        }
//    }
//
//    public final class Recycler {
//        final ArrayList<CoinRecyclerView.ViewHolder> mAttachedScrap = new ArrayList<>();
//        ArrayList<CoinRecyclerView.ViewHolder> mChangedScrap = null;
//
//        final ArrayList<CoinRecyclerView.ViewHolder> mCachedViews = new ArrayList<CoinRecyclerView.ViewHolder>();
//
//        private final List<CoinRecyclerView.ViewHolder>
//                mUnmodifiableAttachedScrap = Collections.unmodifiableList(mAttachedScrap);
//
//        private int mRequestedCacheMax = DEFAULT_CACHE_SIZE;
//        int mViewCacheMax = DEFAULT_CACHE_SIZE;
//
//        RecycledViewPool mRecyclerPool;
//
//        private ViewCacheExtension mViewCacheExtension;
//
//        static final int DEFAULT_CACHE_SIZE = 2;
//
//        public void clear() {
//            mAttachedScrap.clear();
//            recycleAndClearCachedViews();
//        }
//
//        public void setViewCacheSize(int viewCount) {
//            mRequestedCacheMax = viewCount;
//            updateViewCacheSize();
//        }
//
//        void updateViewCacheSize() {
//            int extraCache = mLayout != null ? mLayout.mPrefetchMaxCountObserved : 0;
//            mViewCacheMax = mRequestedCacheMax + extraCache;
//
//            // first, try the views that can be recycled
//            for (int i = mCachedViews.size() - 1;
//                 i >= 0 && mCachedViews.size() > mViewCacheMax; i--) {
//                recycleCachedViewAt(i);
//            }
//        }
//
//        @NonNull
//        public List<CoinRecyclerView.ViewHolder> getScrapList() {
//            return mUnmodifiableAttachedScrap;
//        }
//        boolean validateViewHolderForOffsetPosition(CoinRecyclerView.ViewHolder holder) {
//            if (holder.isRemoved()) {
//                if (DEBUG && !mState.isPreLayout()) {
//                    throw new IllegalStateException("should not receive a removed view unless it"
//                            + " is pre layout" + exceptionLabel());
//                }
//                return mState.isPreLayout();
//            }
//            if (holder.mPosition < 0 || holder.mPosition >= mAdapter.getItemCount()) {
//                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid view holder "
//                        + "adapter position" + holder + exceptionLabel());
//            }
//            if (!mState.isPreLayout()) {
//                // don't check type if it is pre-layout.
//                final int type = mAdapter.getItemViewType(holder.mPosition);
//                if (type != holder.getItemViewType()) {
//                    return false;
//                }
//            }
//            if (mAdapter.hasStableIds()) {
//                return holder.getItemId() == mAdapter.getItemId(holder.mPosition);
//            }
//            return true;
//        }
//
//        @SuppressWarnings("unchecked")
//        private boolean tryBindViewHolderByDeadline(@NonNull CoinRecyclerView.ViewHolder holder, int offsetPosition,
//                                                    int position, long deadlineNs) {
//            holder.mOwnerRecyclerView = CoinRecyclerView.this;
//            final int viewType = holder.getItemViewType();
//            long startBindNs = getNanoTime();
//            if (deadlineNs != FOREVER_NS
//                    && !mRecyclerPool.willBindInTime(viewType, startBindNs, deadlineNs)) {
//                // abort - we have a deadline we can't meet
//                return false;
//            }
//            mAdapter.bindViewHolder(holder, offsetPosition);
//            long endBindNs = getNanoTime();
//            mRecyclerPool.factorInBindTime(holder.getItemViewType(), endBindNs - startBindNs);
//            attachAccessibilityDelegateOnBind(holder);
//            if (mState.isPreLayout()) {
//                holder.mPreLayoutPosition = position;
//            }
//            return true;
//        }
//
//        public void bindViewToPosition(@NonNull View view, int position) {
//            CoinRecyclerView.ViewHolder holder = getChildViewHolderInt(view);
//            if (holder == null) {
//                throw new IllegalArgumentException("The view does not have a ViewHolder. You cannot"
//                        + " pass arbitrary views to this method, they should be created by the "
//                        + "Adapter" + exceptionLabel());
//            }
//            final int offsetPosition = mAdapterHelper.findPositionOffset(position);
//            if (offsetPosition < 0 || offsetPosition >= mAdapter.getItemCount()) {
//                throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item "
//                        + "position " + position + "(offset:" + offsetPosition + ")."
//                        + "state:" + mState.getItemCount() + exceptionLabel());
//            }
//            tryBindViewHolderByDeadline(holder, offsetPosition, position, FOREVER_NS);
//
//            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//            final CoinRecyclerView.LayoutParams rvLayoutParams;
//            if (lp == null) {
//                rvLayoutParams = (CoinRecyclerView.LayoutParams) generateDefaultLayoutParams();
//                holder.itemView.setLayoutParams(rvLayoutParams);
//            } else if (!checkLayoutParams(lp)) {
//                rvLayoutParams = (CoinRecyclerView.LayoutParams) generateLayoutParams(lp);
//                holder.itemView.setLayoutParams(rvLayoutParams);
//            } else {
//                rvLayoutParams = (CoinRecyclerView.LayoutParams) lp;
//            }
//
//            rvLayoutParams.mInsetsDirty = true;
//            rvLayoutParams.mViewHolder = holder;
//            rvLayoutParams.mPendingInvalidate = holder.itemView.getParent() == null;
//        }
//
//        public int convertPreLayoutPositionToPostLayout(int position) {
//            if (position < 0 || position >= mState.getItemCount()) {
//                throw new IndexOutOfBoundsException("invalid position " + position + ". State "
//                        + "item count is " + mState.getItemCount() + exceptionLabel());
//            }
//            if (!mState.isPreLayout()) {
//                return position;
//            }
//            return mAdapterHelper.findPositionOffset(position);
//        }
//
//        @NonNull
//        public View getViewForPosition(int position) {
//            return getViewForPosition(position, false);
//        }
//
//        View getViewForPosition(int position, boolean dryRun) {
//            return tryGetViewHolderForPositionByDeadline(position, dryRun, FOREVER_NS).itemView;
//        }
//
//        @Nullable
//        CoinRecyclerView.ViewHolder tryGetViewHolderForPositionByDeadline(int position,
//                                                                      boolean dryRun, long deadlineNs) {
//            if (position < 0 || position >= mState.getItemCount()) {
//                throw new IndexOutOfBoundsException("Invalid item position " + position
//                        + "(" + position + "). Item count:" + mState.getItemCount()
//                        + exceptionLabel());
//            }
//            boolean fromScrapOrHiddenOrCache = false;
//            CoinRecyclerView.ViewHolder holder = null;
//            // 0) If there is a changed scrap, try to find from there
//            if (mState.isPreLayout()) {
//                holder = getChangedScrapViewForPosition(position);
//                fromScrapOrHiddenOrCache = holder != null;
//            }
//            // 1) Find by position from scrap/hidden list/cache
//            if (holder == null) {
//                holder = getScrapOrHiddenOrCachedHolderForPosition(position, dryRun);
//                if (holder != null) {
//                    if (!validateViewHolderForOffsetPosition(holder)) {
//                        // recycle holder (and unscrap if relevant) since it can't be used
//                        if (!dryRun) {
//                            // we would like to recycle this but need to make sure it is not used by
//                            // animation logic etc.
//                            holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_INVALID);
//                            if (holder.isScrap()) {
//                                removeDetachedView(holder.itemView, false);
//                                holder.unScrap();
//                            } else if (holder.wasReturnedFromScrap()) {
//                                holder.clearReturnedFromScrapFlag();
//                            }
//                            recycleViewHolderInternal(holder);
//                        }
//                        holder = null;
//                    } else {
//                        fromScrapOrHiddenOrCache = true;
//                    }
//                }
//            }
//            if (holder == null) {
//                final int offsetPosition = mAdapterHelper.findPositionOffset(position);
//                if (offsetPosition < 0 || offsetPosition >= mAdapter.getItemCount()) {
//                    throw new IndexOutOfBoundsException("Inconsistency detected. Invalid item "
//                            + "position " + position + "(offset:" + offsetPosition + ")."
//                            + "state:" + mState.getItemCount() + exceptionLabel());
//                }
//
//                final int type = mAdapter.getItemViewType(offsetPosition);
//                // 2) Find from scrap/cache via stable ids, if exists
//                if (mAdapter.hasStableIds()) {
//                    holder = getScrapOrCachedViewForId(mAdapter.getItemId(offsetPosition),
//                            type, dryRun);
//                    if (holder != null) {
//                        // update position
//                        holder.mPosition = offsetPosition;
//                        fromScrapOrHiddenOrCache = true;
//                    }
//                }
//                if (holder == null && mViewCacheExtension != null) {
//                    // We are NOT sending the offsetPosition because LayoutManager does not
//                    // know it.
//                    final View view = mViewCacheExtension
//                            .getViewForPositionAndType(this, position, type);
//                    if (view != null) {
//                        holder = getChildViewHolder(view);
//                        if (holder == null) {
//                            throw new IllegalArgumentException("getViewForPositionAndType returned"
//                                    + " a view which does not have a ViewHolder"
//                                    + exceptionLabel());
//                        } else if (holder.shouldIgnore()) {
//                            throw new IllegalArgumentException("getViewForPositionAndType returned"
//                                    + " a view that is ignored. You must call stopIgnoring before"
//                                    + " returning this view." + exceptionLabel());
//                        }
//                    }
//                }
//                if (holder == null) { // fallback to pool
//                    if (DEBUG) {
//                        Log.d(TAG, "tryGetViewHolderForPositionByDeadline("
//                                + position + ") fetching from shared pool");
//                    }
//                    holder = getRecycledViewPool().getRecycledView(type);
//                    if (holder != null) {
//                        holder.resetInternal();
//                        if (FORCE_INVALIDATE_DISPLAY_LIST) {
//                            invalidateDisplayListInt(holder);
//                        }
//                    }
//                }
//                if (holder == null) {
//                    long start = getNanoTime();
//                    if (deadlineNs != FOREVER_NS
//                            && !mRecyclerPool.willCreateInTime(type, start, deadlineNs)) {
//                        // abort - we have a deadline we can't meet
//                        return null;
//                    }
//                    holder = mAdapter.createViewHolder(CoinRecyclerView.this, type);
//                    if (ALLOW_THREAD_GAP_WORK) {
//                        // only bother finding nested RV if prefetching
//                        CoinRecyclerView innerView = findNestedRecyclerView(holder.itemView);
//                        if (innerView != null) {
//                            holder.mNestedRecyclerView = new WeakReference<>(innerView);
//                        }
//                    }
//
//                    long end = getNanoTime();
//                    mRecyclerPool.factorInCreateTime(type, end - start);
//                    if (DEBUG) {
//                        Log.d(TAG, "tryGetViewHolderForPositionByDeadline created new ViewHolder");
//                    }
//                }
//            }
//
//            if (fromScrapOrHiddenOrCache && !mState.isPreLayout() && holder
//                    .hasAnyOfTheFlags(CoinRecyclerView.ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST)) {
//                holder.setFlags(0, CoinRecyclerView.ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST);
//                if (mState.mRunSimpleAnimations) {
//                    int changeFlags = ItemAnimator
//                            .buildAdapterChangeFlagsForAnimations(holder);
//                    changeFlags |= ItemAnimator.FLAG_APPEARED_IN_PRE_LAYOUT;
//                    final ItemHolderInfo info = mItemAnimator.recordPreLayoutInformation(mState,
//                            holder, changeFlags, holder.getUnmodifiedPayloads());
//                    recordAnimationInfoIfBouncedHiddenView(holder, info);
//                }
//            }
//
//            boolean bound = false;
//            if (mState.isPreLayout() && holder.isBound()) {
//                // do not update unless we absolutely have to.
//                holder.mPreLayoutPosition = position;
//            } else if (!holder.isBound() || holder.needsUpdate() || holder.isInvalid()) {
//                if (DEBUG && holder.isRemoved()) {
//                    throw new IllegalStateException("Removed holder should be bound and it should"
//                            + " come here only in pre-layout. Holder: " + holder
//                            + exceptionLabel());
//                }
//                final int offsetPosition = mAdapterHelper.findPositionOffset(position);
//                bound = tryBindViewHolderByDeadline(holder, offsetPosition, position, deadlineNs);
//            }
//
//            final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//            final CoinRecyclerView.LayoutParams rvLayoutParams;
//            if (lp == null) {
//                rvLayoutParams = (CoinRecyclerView.LayoutParams) generateDefaultLayoutParams();
//                holder.itemView.setLayoutParams(rvLayoutParams);
//            } else if (!checkLayoutParams(lp)) {
//                rvLayoutParams = (CoinRecyclerView.LayoutParams) generateLayoutParams(lp);
//                holder.itemView.setLayoutParams(rvLayoutParams);
//            } else {
//                rvLayoutParams = (CoinRecyclerView.LayoutParams) lp;
//            }
//            rvLayoutParams.mViewHolder = holder;
//            rvLayoutParams.mPendingInvalidate = fromScrapOrHiddenOrCache && bound;
//            return holder;
//        }
//
//        private void attachAccessibilityDelegateOnBind(CoinRecyclerView.ViewHolder holder) {
//            if (isAccessibilityEnabled()) {
//                final View itemView = holder.itemView;
//                if (ViewCompat.getImportantForAccessibility(itemView)
//                        == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
//                    ViewCompat.setImportantForAccessibility(itemView,
//                            ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
//                }
//                if (mAccessibilityDelegate == null) {
//                    return;
//                }
//                AccessibilityDelegateCompat itemDelegate = mAccessibilityDelegate.getItemDelegate();
//                if (itemDelegate instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
//                    // If there was already an a11y delegate set on the itemView, store it in the
//                    // itemDelegate and then set the itemDelegate as the a11y delegate.
//                    ((RecyclerViewAccessibilityDelegate.ItemDelegate) itemDelegate)
//                            .saveOriginalDelegate(itemView);
//                }
//                ViewCompat.setAccessibilityDelegate(itemView, itemDelegate);
//            }
//        }
//
//        private void invalidateDisplayListInt(CoinRecyclerView.ViewHolder holder) {
//            if (holder.itemView instanceof ViewGroup) {
//                invalidateDisplayListInt((ViewGroup) holder.itemView, false);
//            }
//        }
//
//        private void invalidateDisplayListInt(ViewGroup viewGroup, boolean invalidateThis) {
//            for (int i = viewGroup.getChildCount() - 1; i >= 0; i--) {
//                final View view = viewGroup.getChildAt(i);
//                if (view instanceof ViewGroup) {
//                    invalidateDisplayListInt((ViewGroup) view, true);
//                }
//            }
//            if (!invalidateThis) {
//                return;
//            }
//            // we need to force it to become invisible
//            if (viewGroup.getVisibility() == View.INVISIBLE) {
//                viewGroup.setVisibility(View.VISIBLE);
//                viewGroup.setVisibility(View.INVISIBLE);
//            } else {
//                final int visibility = viewGroup.getVisibility();
//                viewGroup.setVisibility(View.INVISIBLE);
//                viewGroup.setVisibility(visibility);
//            }
//        }
//
//        public void recycleView(@NonNull View view) {
//            CoinRecyclerView.ViewHolder holder = getChildViewHolderInt(view);
//            if (holder.isTmpDetached()) {
//                removeDetachedView(view, false);
//            }
//            if (holder.isScrap()) {
//                holder.unScrap();
//            } else if (holder.wasReturnedFromScrap()) {
//                holder.clearReturnedFromScrapFlag();
//            }
//            recycleViewHolderInternal(holder);
//
//            if (mItemAnimator != null && !holder.isRecyclable()) {
//                mItemAnimator.endAnimation(holder);
//            }
//        }
//
//        void recycleAndClearCachedViews() {
//            final int count = mCachedViews.size();
//            for (int i = count - 1; i >= 0; i--) {
//                recycleCachedViewAt(i);
//            }
//            mCachedViews.clear();
//            if (ALLOW_THREAD_GAP_WORK) {
//                mPrefetchRegistry.clearPrefetchPositions();
//            }
//        }
//
//        void recycleCachedViewAt(int cachedViewIndex) {
//            if (DEBUG) {
//                Log.d(TAG, "Recycling cached view at index " + cachedViewIndex);
//            }
//            CoinRecyclerView.ViewHolder viewHolder = mCachedViews.get(cachedViewIndex);
//            if (DEBUG) {
//                Log.d(TAG, "CachedViewHolder to be recycled: " + viewHolder);
//            }
//            addViewHolderToRecycledViewPool(viewHolder, true);
//            mCachedViews.remove(cachedViewIndex);
//        }
//
//        void recycleViewHolderInternal(CoinRecyclerView.ViewHolder holder) {
//            if (holder.isScrap() || holder.itemView.getParent() != null) {
//                throw new IllegalArgumentException(
//                        "Scrapped or attached views may not be recycled. isScrap:"
//                                + holder.isScrap() + " isAttached:"
//                                + (holder.itemView.getParent() != null) + exceptionLabel());
//            }
//
//            if (holder.isTmpDetached()) {
//                throw new IllegalArgumentException("Tmp detached view should be removed "
//                        + "from CoinRecyclerView before it can be recycled: " + holder
//                        + exceptionLabel());
//            }
//
//            if (holder.shouldIgnore()) {
//                throw new IllegalArgumentException("Trying to recycle an ignored view holder. You"
//                        + " should first call stopIgnoringView(view) before calling recycle."
//                        + exceptionLabel());
//            }
//            final boolean transientStatePreventsRecycling = holder
//                    .doesTransientStatePreventRecycling();
//            @SuppressWarnings("unchecked")
//            final boolean forceRecycle = mAdapter != null
//                    && transientStatePreventsRecycling
//                    && mAdapter.onFailedToRecycleView(holder);
//            boolean cached = false;
//            boolean recycled = false;
//            if (DEBUG && mCachedViews.contains(holder)) {
//                throw new IllegalArgumentException("cached view received recycle internal? "
//                        + holder + exceptionLabel());
//            }
//            if (forceRecycle || holder.isRecyclable()) {
//                if (mViewCacheMax > 0
//                        && !holder.hasAnyOfTheFlags(CoinRecyclerView.ViewHolder.FLAG_INVALID
//                        | CoinRecyclerView.ViewHolder.FLAG_REMOVED
//                        | CoinRecyclerView.ViewHolder.FLAG_UPDATE
//                        | CoinRecyclerView.ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN)) {
//                    // Retire oldest cached view
//                    int cachedViewSize = mCachedViews.size();
//                    if (cachedViewSize >= mViewCacheMax && cachedViewSize > 0) {
//                        recycleCachedViewAt(0);
//                        cachedViewSize--;
//                    }
//
//                    int targetCacheIndex = cachedViewSize;
//                    if (ALLOW_THREAD_GAP_WORK
//                            && cachedViewSize > 0
//                            && !mPrefetchRegistry.lastPrefetchIncludedPosition(holder.mPosition)) {
//                        // when adding the view, skip past most recently prefetched views
//                        int cacheIndex = cachedViewSize - 1;
//                        while (cacheIndex >= 0) {
//                            int cachedPos = mCachedViews.get(cacheIndex).mPosition;
//                            if (!mPrefetchRegistry.lastPrefetchIncludedPosition(cachedPos)) {
//                                break;
//                            }
//                            cacheIndex--;
//                        }
//                        targetCacheIndex = cacheIndex + 1;
//                    }
//                    mCachedViews.add(targetCacheIndex, holder);
//                    cached = true;
//                }
//                if (!cached) {
//                    addViewHolderToRecycledViewPool(holder, true);
//                    recycled = true;
//                }
//            } else {
//
//                if (DEBUG) {
//                    Log.d(TAG, "trying to recycle a non-recycleable holder. Hopefully, it will "
//                            + "re-visit here. We are still removing it from animation lists"
//                            + exceptionLabel());
//                }
//            }
//
//            mViewInfoStore.removeViewHolder(holder);
//            if (!cached && !recycled && transientStatePreventsRecycling) {
//                holder.mOwnerRecyclerView = null;
//            }
//        }
//
//        void addViewHolderToRecycledViewPool(@NonNull CoinRecyclerView.ViewHolder holder, boolean dispatchRecycled) {
//            clearNestedRecyclerViewIfNotNested(holder);
//            View itemView = holder.itemView;
//            if (mAccessibilityDelegate != null) {
//                AccessibilityDelegateCompat itemDelegate = mAccessibilityDelegate.getItemDelegate();
//                AccessibilityDelegateCompat originalDelegate = null;
//                if (itemDelegate instanceof RecyclerViewAccessibilityDelegate.ItemDelegate) {
//                    originalDelegate =
//                            ((RecyclerViewAccessibilityDelegate.ItemDelegate) itemDelegate)
//                                    .getAndRemoveOriginalDelegateForItem(itemView);
//                }
//                // Set the a11y delegate back to whatever the original delegate was.
//                ViewCompat.setAccessibilityDelegate(itemView, originalDelegate);
//            }
//            if (dispatchRecycled) {
//                dispatchViewRecycled(holder);
//            }
//            holder.mOwnerRecyclerView = null;
//            getRecycledViewPool().putRecycledView(holder);
//        }
//
//        void quickRecycleScrapView(View view) {
//            final CoinRecyclerView.ViewHolder holder = getChildViewHolderInt(view);
//            holder.mScrapContainer = null;
//            holder.mInChangeScrap = false;
//            holder.clearReturnedFromScrapFlag();
//            recycleViewHolderInternal(holder);
//        }
//
//        void scrapView(View view) {
//            final CoinRecyclerView.ViewHolder holder = getChildViewHolderInt(view);
//            if (holder.hasAnyOfTheFlags(CoinRecyclerView.ViewHolder.FLAG_REMOVED | CoinRecyclerView.ViewHolder.FLAG_INVALID)
//                    || !holder.isUpdated() || canReuseUpdatedViewHolder(holder)) {
//                if (holder.isInvalid() && !holder.isRemoved() && !mAdapter.hasStableIds()) {
//                    throw new IllegalArgumentException("Called scrap view with an invalid view."
//                            + " Invalid views cannot be reused from scrap, they should rebound from"
//                            + " recycler pool." + exceptionLabel());
//                }
//                holder.setScrapContainer(this, false);
//                mAttachedScrap.add(holder);
//            } else {
//                if (mChangedScrap == null) {
//                    mChangedScrap = new ArrayList<CoinRecyclerView.ViewHolder>();
//                }
//                holder.setScrapContainer(this, true);
//                mChangedScrap.add(holder);
//            }
//        }
//
//        void unscrapView(CoinRecyclerView.ViewHolder holder) {
//            if (holder.mInChangeScrap) {
//                mChangedScrap.remove(holder);
//            } else {
//                mAttachedScrap.remove(holder);
//            }
//            holder.mScrapContainer = null;
//            holder.mInChangeScrap = false;
//            holder.clearReturnedFromScrapFlag();
//        }
//
//        int getScrapCount() {
//            return mAttachedScrap.size();
//        }
//
//        View getScrapViewAt(int index) {
//            return mAttachedScrap.get(index).itemView;
//        }
//
//        void clearScrap() {
//            mAttachedScrap.clear();
//            if (mChangedScrap != null) {
//                mChangedScrap.clear();
//            }
//        }
//
//        CoinRecyclerView.ViewHolder getChangedScrapViewForPosition(int position) {
//            // If pre-layout, check the changed scrap for an exact match.
//            final int changedScrapSize;
//            if (mChangedScrap == null || (changedScrapSize = mChangedScrap.size()) == 0) {
//                return null;
//            }
//            // find by position
//            for (int i = 0; i < changedScrapSize; i++) {
//                final CoinRecyclerView.ViewHolder holder = mChangedScrap.get(i);
//                if (!holder.wasReturnedFromScrap() && holder.getLayoutPosition() == position) {
//                    holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_RETURNED_FROM_SCRAP);
//                    return holder;
//                }
//            }
//            // find by id
//            if (mAdapter.hasStableIds()) {
//                final int offsetPosition = mAdapterHelper.findPositionOffset(position);
//                if (offsetPosition > 0 && offsetPosition < mAdapter.getItemCount()) {
//                    final long id = mAdapter.getItemId(offsetPosition);
//                    for (int i = 0; i < changedScrapSize; i++) {
//                        final CoinRecyclerView.ViewHolder holder = mChangedScrap.get(i);
//                        if (!holder.wasReturnedFromScrap() && holder.getItemId() == id) {
//                            holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_RETURNED_FROM_SCRAP);
//                            return holder;
//                        }
//                    }
//                }
//            }
//            return null;
//        }
//
//       CoinRecyclerView.ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int position, boolean dryRun) {
//            final int scrapCount = mAttachedScrap.size();
//
//            // Try first for an exact, non-invalid match from scrap.
//            for (int i = 0; i < scrapCount; i++) {
//                final CoinRecyclerView.ViewHolder holder = mAttachedScrap.get(i);
//                if (!holder.wasReturnedFromScrap() && holder.getLayoutPosition() == position
//                        && !holder.isInvalid() && (mState.mInPreLayout || !holder.isRemoved())) {
//                    holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_RETURNED_FROM_SCRAP);
//                    return holder;
//                }
//            }
//
//            if (!dryRun) {
//                View view = mChildHelper.findHiddenNonRemovedView(position);
//                if (view != null) {
//                    final CoinRecyclerView.ViewHolder vh = getChildViewHolderInt(view);
//                    mChildHelper.unhide(view);
//                    int layoutIndex = mChildHelper.indexOfChild(view);
//                    if (layoutIndex == CoinRecyclerView.NO_POSITION) {
//                        throw new IllegalStateException("layout index should not be -1 after "
//                                + "unhiding a view:" + vh + exceptionLabel());
//                    }
//                    mChildHelper.detachViewFromParent(layoutIndex);
//                    scrapView(view);
//                    vh.addFlags(CoinRecyclerView.ViewHolder.FLAG_RETURNED_FROM_SCRAP
//                            | CoinRecyclerView.ViewHolder.FLAG_BOUNCED_FROM_HIDDEN_LIST);
//                    return vh;
//                }
//            }
//
//            final int cacheSize = mCachedViews.size();
//            for (int i = 0; i < cacheSize; i++) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                if (!holder.isInvalid() && holder.getLayoutPosition() == position
//                        && !holder.isAttachedToTransitionOverlay()) {
//                    if (!dryRun) {
//                        mCachedViews.remove(i);
//                    }
//                    if (DEBUG) {
//                        Log.d(TAG, "getScrapOrHiddenOrCachedHolderForPosition(" + position
//                                + ") found match in cache: " + holder);
//                    }
//                    return holder;
//                }
//            }
//            return null;
//        }
//
//        CoinRecyclerView.ViewHolder getScrapOrCachedViewForId(long id, int type, boolean dryRun) {
//            // Look in our attached views first
//            final int count = mAttachedScrap.size();
//            for (int i = count - 1; i >= 0; i--) {
//                final CoinRecyclerView.ViewHolder holder = mAttachedScrap.get(i);
//                if (holder.getItemId() == id && !holder.wasReturnedFromScrap()) {
//                    if (type == holder.getItemViewType()) {
//                        holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_RETURNED_FROM_SCRAP);
//                        if (holder.isRemoved()) {
//                            if (!mState.isPreLayout()) {
//                                holder.setFlags(CoinRecyclerView.ViewHolder.FLAG_UPDATE, CoinRecyclerView.ViewHolder.FLAG_UPDATE
//                                        | CoinRecyclerView.ViewHolder.FLAG_INVALID | CoinRecyclerView.ViewHolder.FLAG_REMOVED);
//                            }
//                        }
//                        return holder;
//                    } else if (!dryRun) {
//                        mAttachedScrap.remove(i);
//                        removeDetachedView(holder.itemView, false);
//                        quickRecycleScrapView(holder.itemView);
//                    }
//                }
//            }
//
//            // Search the first-level cache
//            final int cacheSize = mCachedViews.size();
//            for (int i = cacheSize - 1; i >= 0; i--) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                if (holder.getItemId() == id && !holder.isAttachedToTransitionOverlay()) {
//                    if (type == holder.getItemViewType()) {
//                        if (!dryRun) {
//                            mCachedViews.remove(i);
//                        }
//                        return holder;
//                    } else if (!dryRun) {
//                        recycleCachedViewAt(i);
//                        return null;
//                    }
//                }
//            }
//            return null;
//        }
//
//        @SuppressWarnings("unchecked")
//        void dispatchViewRecycled(@NonNull CoinRecyclerView.ViewHolder holder) {
//            if (mRecyclerListener != null) {
//                mRecyclerListener.onViewRecycled(holder);
//            }
//            if (mAdapter != null) {
//                mAdapter.onViewRecycled(holder);
//            }
//            if (mState != null) {
//                mViewInfoStore.removeViewHolder(holder);
//            }
//            if (DEBUG) Log.d(TAG, "dispatchViewRecycled: " + holder);
//        }
//
//        void onAdapterChanged(CoinRecyclerView.Adapter oldAdapter, CoinRecyclerView.Adapter newAdapter,
//                              boolean compatibleWithPrevious) {
//            clear();
//            getRecycledViewPool().onAdapterChanged(oldAdapter, newAdapter, compatibleWithPrevious);
//        }
//
//        void offsetPositionRecordsForMove(int from, int to) {
//            final int start, end, inBetweenOffset;
//            if (from < to) {
//                start = from;
//                end = to;
//                inBetweenOffset = -1;
//            } else {
//                start = to;
//                end = from;
//                inBetweenOffset = 1;
//            }
//            final int cachedCount = mCachedViews.size();
//            for (int i = 0; i < cachedCount; i++) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                if (holder == null || holder.mPosition < start || holder.mPosition > end) {
//                    continue;
//                }
//                if (holder.mPosition == from) {
//                    holder.offsetPosition(to - from, false);
//                } else {
//                    holder.offsetPosition(inBetweenOffset, false);
//                }
//                if (DEBUG) {
//                    Log.d(TAG, "offsetPositionRecordsForMove cached child " + i + " holder "
//                            + holder);
//                }
//            }
//        }
//
//        void offsetPositionRecordsForInsert(int insertedAt, int count) {
//            final int cachedCount = mCachedViews.size();
//            for (int i = 0; i < cachedCount; i++) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                if (holder != null && holder.mPosition >= insertedAt) {
//                    if (DEBUG) {
//                        Log.d(TAG, "offsetPositionRecordsForInsert cached " + i + " holder "
//                                + holder + " now at position " + (holder.mPosition + count));
//                    }
//                    holder.offsetPosition(count, true);
//                }
//            }
//        }
//
//        void offsetPositionRecordsForRemove(int removedFrom, int count, boolean applyToPreLayout) {
//            final int removedEnd = removedFrom + count;
//            final int cachedCount = mCachedViews.size();
//            for (int i = cachedCount - 1; i >= 0; i--) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                if (holder != null) {
//                    if (holder.mPosition >= removedEnd) {
//                        if (DEBUG) {
//                            Log.d(TAG, "offsetPositionRecordsForRemove cached " + i
//                                    + " holder " + holder + " now at position "
//                                    + (holder.mPosition - count));
//                        }
//                        holder.offsetPosition(-count, applyToPreLayout);
//                    } else if (holder.mPosition >= removedFrom) {
//                        // Item for this view was removed. Dump it from the cache.
//                        holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_REMOVED);
//                        recycleCachedViewAt(i);
//                    }
//                }
//            }
//        }
//
//        void setViewCacheExtension(ViewCacheExtension extension) {
//            mViewCacheExtension = extension;
//        }
//
//        void setRecycledViewPool(RecycledViewPool pool) {
//            if (mRecyclerPool != null) {
//                mRecyclerPool.detach();
//            }
//            mRecyclerPool = pool;
//            if (mRecyclerPool != null && getAdapter() != null) {
//                mRecyclerPool.attach();
//            }
//        }
//
//        RecycledViewPool getRecycledViewPool() {
//            if (mRecyclerPool == null) {
//                mRecyclerPool = new RecycledViewPool();
//            }
//            return mRecyclerPool;
//        }
//
//        void viewRangeUpdate(int positionStart, int itemCount) {
//            final int positionEnd = positionStart + itemCount;
//            final int cachedCount = mCachedViews.size();
//            for (int i = cachedCount - 1; i >= 0; i--) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                if (holder == null) {
//                    continue;
//                }
//
//                final int pos = holder.mPosition;
//                if (pos >= positionStart && pos < positionEnd) {
//                    holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_UPDATE);
//                    recycleCachedViewAt(i);
//                    // cached views should not be flagged as changed because this will cause them
//                    // to animate when they are returned from cache.
//                }
//            }
//        }
//
//        void markKnownViewsInvalid() {
//            final int cachedCount = mCachedViews.size();
//            for (int i = 0; i < cachedCount; i++) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                if (holder != null) {
//                    holder.addFlags(CoinRecyclerView.ViewHolder.FLAG_UPDATE | CoinRecyclerView.ViewHolder.FLAG_INVALID);
//                    holder.addChangePayload(null);
//                }
//            }
//
//            if (mAdapter == null || !mAdapter.hasStableIds()) {
//                // we cannot re-use cached views in this case. Recycle them all
//                recycleAndClearCachedViews();
//            }
//        }
//
//        void clearOldPositions() {
//            final int cachedCount = mCachedViews.size();
//            for (int i = 0; i < cachedCount; i++) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                holder.clearOldPosition();
//            }
//            final int scrapCount = mAttachedScrap.size();
//            for (int i = 0; i < scrapCount; i++) {
//                mAttachedScrap.get(i).clearOldPosition();
//            }
//            if (mChangedScrap != null) {
//                final int changedScrapCount = mChangedScrap.size();
//                for (int i = 0; i < changedScrapCount; i++) {
//                    mChangedScrap.get(i).clearOldPosition();
//                }
//            }
//        }
//
//        void markItemDecorInsetsDirty() {
//            final int cachedCount = mCachedViews.size();
//            for (int i = 0; i < cachedCount; i++) {
//                final CoinRecyclerView.ViewHolder holder = mCachedViews.get(i);
//                CoinRecyclerView.LayoutParams layoutParams = (CoinRecyclerView.LayoutParams) holder.itemView.getLayoutParams();
//                if (layoutParams != null) {
//                    layoutParams.mInsetsDirty = true;
//                }
//            }
//        }
//    }
//
//    static class AdapterDataObservable extends Observable<AdapterDataObserver> {
//        public boolean hasObservers() {
//            return !mObservers.isEmpty();
//        }
//
//        public void notifyChanged() {
//            // since onChanged() is implemented by the app, it could do anything, including
//            // removing itself from {@link mObservers} - and that could cause problems if
//            // an iterator is used on the ArrayList {@link mObservers}.
//            // to avoid such problems, just march thru the list in the reverse order.
//            for (int i = mObservers.size() - 1; i >= 0; i--) {
//                mObservers.get(i).onChanged();
//            }
//        }
//
//        public void notifyItemRangeChanged(int positionStart, int itemCount) {
//            notifyItemRangeChanged(positionStart, itemCount, null);
//        }
//
//        public void notifyItemRangeChanged(int positionStart, int itemCount,
//                                           @Nullable Object payload) {
//            // since onItemRangeChanged() is implemented by the app, it could do anything, including
//            // removing itself from {@link mObservers} - and that could cause problems if
//            // an iterator is used on the ArrayList {@link mObservers}.
//            // to avoid such problems, just march thru the list in the reverse order.
//            for (int i = mObservers.size() - 1; i >= 0; i--) {
//                mObservers.get(i).onItemRangeChanged(positionStart, itemCount, payload);
//            }
//        }
//
//        public void notifyItemRangeInserted(int positionStart, int itemCount) {
//            // since onItemRangeInserted() is implemented by the app, it could do anything,
//            // including removing itself from {@link mObservers} - and that could cause problems if
//            // an iterator is used on the ArrayList {@link mObservers}.
//            // to avoid such problems, just march thru the list in the reverse order.
//            for (int i = mObservers.size() - 1; i >= 0; i--) {
//                mObservers.get(i).onItemRangeInserted(positionStart, itemCount);
//            }
//        }
//
//        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
//            // since onItemRangeRemoved() is implemented by the app, it could do anything, including
//            // removing itself from {@link mObservers} - and that could cause problems if
//            // an iterator is used on the ArrayList {@link mObservers}.
//            // to avoid such problems, just march thru the list in the reverse order.
//            for (int i = mObservers.size() - 1; i >= 0; i--) {
//                mObservers.get(i).onItemRangeRemoved(positionStart, itemCount);
//            }
//        }
//
//        public void notifyItemMoved(int fromPosition, int toPosition) {
//            for (int i = mObservers.size() - 1; i >= 0; i--) {
//                mObservers.get(i).onItemRangeMoved(fromPosition, toPosition, 1);
//            }
//        }
//    }
//
//    public abstract static class AdapterDataObserver {
//        public void onChanged() {
//            // Do nothing
//        }
//
//        public void onItemRangeChanged(int positionStart, int itemCount) {
//            // do nothing
//        }
//
//        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
//            // fallback to onItemRangeChanged(positionStart, itemCount) if app
//            // does not override this method.
//            onItemRangeChanged(positionStart, itemCount);
//        }
//
//        public void onItemRangeInserted(int positionStart, int itemCount) {
//            // do nothing
//        }
//
//        public void onItemRangeRemoved(int positionStart, int itemCount) {
//            // do nothing
//        }
//
//        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
//            // do nothing
//        }
//    }
//
//    int getAdapterPositionFor(CoinRecyclerView.ViewHolder viewHolder) {
//        if (viewHolder.hasAnyOfTheFlags(CoinRecyclerView.ViewHolder.FLAG_INVALID
//                | CoinRecyclerView.ViewHolder.FLAG_REMOVED | CoinRecyclerView.ViewHolder.FLAG_ADAPTER_POSITION_UNKNOWN)
//                || !viewHolder.isBound()) {
//            return CoinRecyclerView.NO_POSITION;
//        }
//        return mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
//    }
//
//    void initAdapterManager() {
//        mAdapterHelper = new AdapterHelper(new AdapterHelper.Callback() {
//            @Override
//            public CoinRecyclerView.ViewHolder findViewHolder(int position) {
//                final CoinRecyclerView.ViewHolder vh = findViewHolderForPosition(position, true);
//                if (vh == null) {
//                    return null;
//                }
//                // ensure it is not hidden because for adapter helper, the only thing matter is that
//                // LM thinks view is a child.
//                if (mChildHelper.isHidden(vh.itemView)) {
//                    if (DEBUG) {
//                        Log.d(TAG, "assuming view holder cannot be find because it is hidden");
//                    }
//                    return null;
//                }
//                return vh;
//            }
//
//            @Override
//            public void offsetPositionsForRemovingInvisible(int start, int count) {
//                offsetPositionRecordsForRemove(start, count, true);
//                mItemsAddedOrRemoved = true;
//                mState.mDeletedInvisibleItemCountSincePreviousLayout += count;
//            }
//
//            @Override
//            public void offsetPositionsForRemovingLaidOutOrNewView(
//                    int positionStart, int itemCount) {
//                offsetPositionRecordsForRemove(positionStart, itemCount, false);
//                mItemsAddedOrRemoved = true;
//            }
//
//
//            @Override
//            public void markViewHoldersUpdated(int positionStart, int itemCount, Object payload) {
//                viewRangeUpdate(positionStart, itemCount, payload);
//                mItemsChanged = true;
//            }
//
//            @Override
//            public void onDispatchFirstPass(AdapterHelper.UpdateOp op) {
//                dispatchUpdate(op);
//            }
//
//            void dispatchUpdate(AdapterHelper.UpdateOp op) {
//                switch (op.cmd) {
//                    case AdapterHelper.UpdateOp.ADD:
//                        mLayout.onItemsAdded(CoinRecyclerView.this, op.positionStart, op.itemCount);
//                        break;
//                    case AdapterHelper.UpdateOp.REMOVE:
//                        mLayout.onItemsRemoved(CoinRecyclerView.this, op.positionStart, op.itemCount);
//                        break;
//                    case AdapterHelper.UpdateOp.UPDATE:
//                        mLayout.onItemsUpdated(CoinRecyclerView.this, op.positionStart, op.itemCount,
//                                op.payload);
//                        break;
//                    case AdapterHelper.UpdateOp.MOVE:
//                        mLayout.onItemsMoved(CoinRecyclerView.this, op.positionStart, op.itemCount, 1);
//                        break;
//                }
//            }
//
//            @Override
//            public void onDispatchSecondPass(AdapterHelper.UpdateOp op) {
//                dispatchUpdate(op);
//            }
//
//            @Override
//            public void offsetPositionsForAdd(int positionStart, int itemCount) {
//                offsetPositionRecordsForInsert(positionStart, itemCount);
//                mItemsAddedOrRemoved = true;
//            }
//
//            @Override
//            public void offsetPositionsForMove(int from, int to) {
//                offsetPositionRecordsForMove(from, to);
//                // should we create mItemsMoved ?
//                mItemsAddedOrRemoved = true;
//            }
//        });
//    }
//
//    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
//        CoinRecyclerView.ViewHolder mViewHolder;
//        final Rect mDecorInsets = new Rect();
//        boolean mInsetsDirty = true;
//        // Flag is set to true if the view is bound while it is detached from RV.
//        // In this case, we need to manually call invalidate after view is added to guarantee that
//        // invalidation is populated through the View hierarchy
//        boolean mPendingInvalidate = false;
//
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//        }
//
//        public LayoutParams(int width, int height) {
//            super(width, height);
//        }
//
//        public LayoutParams(MarginLayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(ViewGroup.LayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(CoinRecyclerView.LayoutParams source) {
//            super((ViewGroup.LayoutParams) source);
//        }
//
//        /**
//         * Returns true if the view this LayoutParams is attached to needs to have its content
//         * updated from the corresponding adapter.
//         *
//         * @return true if the view should have its content updated
//         */
//        public boolean viewNeedsUpdate() {
//            return mViewHolder.needsUpdate();
//        }
//
//        /**
//         * Returns true if the view this LayoutParams is attached to is now representing
//         * potentially invalid data. A LayoutManager should scrap/recycle it.
//         *
//         * @return true if the view is invalid
//         */
//        public boolean isViewInvalid() {
//            return mViewHolder.isInvalid();
//        }
//
//        /**
//         * Returns true if the adapter data item corresponding to the view this LayoutParams
//         * is attached to has been removed from the data set. A LayoutManager may choose to
//         * treat it differently in order to animate its outgoing or disappearing state.
//         *
//         * @return true if the item the view corresponds to was removed from the data set
//         */
//        public boolean isItemRemoved() {
//            return mViewHolder.isRemoved();
//        }
//
//        /**
//         * Returns true if the adapter data item corresponding to the view this LayoutParams
//         * is attached to has been changed in the data set. A LayoutManager may choose to
//         * treat it differently in order to animate its changing state.
//         *
//         * @return true if the item the view corresponds to was changed in the data set
//         */
//        public boolean isItemChanged() {
//            return mViewHolder.isUpdated();
//        }
//
//        /**
//         * @deprecated use {@link #getViewLayoutPosition()} or {@link #getViewAdapterPosition()}
//         */
//        @Deprecated
//        public int getViewPosition() {
//            return mViewHolder.getPosition();
//        }
//
//        /**
//         * Returns the adapter position that the view this LayoutParams is attached to corresponds
//         * to as of latest layout calculation.
//         *
//         * @return the adapter position this view as of latest layout pass
//         */
//        public int getViewLayoutPosition() {
//            return mViewHolder.getLayoutPosition();
//        }
//
//        /**
//         * Returns the up-to-date adapter position that the view this LayoutParams is attached to
//         * corresponds to.
//         *
//         * @return the up-to-date adapter position this view. It may return
//         * {@link CoinRecyclerView#NO_POSITION} if item represented by this View has been removed or
//         * its up-to-date position cannot be calculated.
//         */
//        public int getViewAdapterPosition() {
//            return mViewHolder.getAdapterPosition();
//        }
//    }
//
//
//
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//
//    }
//
//    @Override
//    public int computeHorizontalScrollRange() {
//        return 0;
//    }
//
//    @Override
//    public int computeHorizontalScrollOffset() {
//        return 0;
//    }
//
//    @Override
//    public int computeHorizontalScrollExtent() {
//        return 0;
//    }
//
//    @Override
//    public int computeVerticalScrollRange() {
//        return 0;
//    }
//
//    @Override
//    public int computeVerticalScrollOffset() {
//        return 0;
//    }
//
//    @Override
//    public int computeVerticalScrollExtent() {
//        return 0;
//    }
//
//    @Override
//    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
//
//    }
//
//    @Override
//    public boolean startNestedScroll(int axes, int type) {
//        return false;
//    }
//
//    @Override
//    public void stopNestedScroll(int type) {
//
//    }
//
//    @Override
//    public boolean hasNestedScrollingParent(int type) {
//        return false;
//    }
//
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type) {
//        return false;
//    }
//
//    @Override
//    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
//        return false;
//    }
//}

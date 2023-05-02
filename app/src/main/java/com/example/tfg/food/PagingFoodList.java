package com.example.tfg.food;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import java.util.List;

import kotlin.coroutines.Continuation;

public class PagingFoodList extends PagingSource<Integer, Food> {
    private List<Food> foodList;

    public PagingFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Nullable
    @Override
    public Object load(@NonNull LoadParams<Integer> loadParams, @NonNull Continuation<? super LoadResult<Integer, Food>> continuation) {
        return null;
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Food> pagingState) {
        return null;
    }
}

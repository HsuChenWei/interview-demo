package com.interview.demo.model.wrapper;

import com.interview.demo.error.ApiError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class RespWrapper<T> {
    @Schema(description = "請求是否成功")
    private Boolean isSuccess;
    @Schema(description = "結果資料。僅在 `isSuccess = true` 時有值。")
    private T data;
    @Schema(description = "分頁資訊。僅在 `isSuccess = true` 時且結果為分頁式資料時有值。")
    private Pagination paging;
    @Schema(description = "錯誤詳細資料。僅在 `isSuccess = false` 時有值。")
    private ApiError error;

    private RespWrapper(T data) {
        this.isSuccess = true;
        this.data = data;
    }

    private <E> RespWrapper(Page<E> page) {
        this.isSuccess = true;
        this.data = (T) page.getContent();
        this.paging = new Pagination(page.getNumber(), page.getSize(), page.getTotalElements());
    }

    private RespWrapper(ApiError error) {
        this.isSuccess = false;
        this.error = error;
    }

    public static <T> RespWrapper<T> success(T data) {
        return new RespWrapper<T>(data);
    }

    public static <T> RespWrapper<List<T>> page(Page<T> page) {
        return new RespWrapper<List<T>>(page);
    }

    public static <T> RespWrapper<T> failure(ApiError error) {
        return new RespWrapper<T>(error);
    }

    @Data
    @AllArgsConstructor
    public static class Pagination {
        @Schema(description = "當前分頁索引 (0-based)")
        private int pageIndex;
        @Schema(description = "當前分頁大小")
        private int pageSize;
        @Schema(description = "資料總比數")
        private long total;


        public int getTotalPages() {
            return getPageSize() == 0 ? 1 : (int) Math.ceil((double) this.total / (double) getPageSize());
        }

        public boolean getHasContent() {
            return total > 0;
        }

        public boolean getHasNext() {
            return getPageIndex() + 1 < this.getTotalPages();
        }

        boolean hasPrevious() {
            return !(getPageIndex() == 0);
        }


        public boolean isFirst() {
            return !hasPrevious();
        }

        public boolean isLast() {
            return !getHasNext();
        }

    }
}

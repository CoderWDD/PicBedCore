package com.example.picbedcore.util

import com.example.picbedcore.dto.PagesDTO
import kotlin.math.ceil

object DTOUtil {
    fun <T> listToPageDTO(list: List<T>, page: Int, size: Int): PagesDTO<T> {
        val currentElements = if (page * size <= list.size) size else if (page == 1 && list.size < size) list.size else list.size % size
        val totalPages = ceil(list.size / size.toDouble()).toInt()
        return PagesDTO(
            hasMore = page * size < list.size,
            totalPages = totalPages,
            totalElements = list.size.toLong(),
            currentPage = page,
            currentElements = currentElements,
            pageSize = size,
            isFirst = page == 1,
            isLast = page == totalPages,
            hasPrevious = page != 1,
            dataList = list
        )
    }
}
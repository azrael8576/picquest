package com.wei.picquest.feature.photo.photosearch.manager

class RecentSearchManager(private val maxCapacity: Int = 10) {
    private val recentSearches = linkedSetOf<String>()

    fun addSearchQuery(query: String) {
        if (query in recentSearches) {
            recentSearches.remove(query)
        }

        recentSearches.add(query)

        if (recentSearches.size > maxCapacity) {
            val oldestQuery = recentSearches.iterator().next()
            recentSearches.remove(oldestQuery)
        }
    }

    fun clearSearches() {
        recentSearches.clear()
    }

    val recentSearchQueries: List<String>
        get() = recentSearches.toList().asReversed()
}

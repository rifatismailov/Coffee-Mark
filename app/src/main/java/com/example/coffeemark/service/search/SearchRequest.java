package com.example.coffeemark.service.search;

public class SearchRequest {
    private final String username;
    private final String search;
    private final String searchBy;

    private SearchRequest(Builder builder) {
        this.username = builder.username;
        this.search = builder.search;
        this.searchBy = builder.searchBy;
    }

    public String getUsername() {
        return username;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public String getSearch() {
        return search;
    }

    // Builder class
    public static class Builder {
        private String username;
        private String search;
        private String searchBy;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setSearch(String search) {
            this.search = search;
            return this;
        }

        public Builder setSearchBy(String searchBy) {
            this.searchBy = searchBy;
            return this;
        }

        public SearchRequest build() {
            return new SearchRequest(this);
        }
    }
}

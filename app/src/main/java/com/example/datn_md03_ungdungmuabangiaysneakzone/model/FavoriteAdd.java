package com.example.datn_md03_ungdungmuabangiaysneakzone.model;

public class FavoriteAdd {
    private boolean isFavorite;

    public FavoriteAdd() {
    }

    public FavoriteAdd(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}

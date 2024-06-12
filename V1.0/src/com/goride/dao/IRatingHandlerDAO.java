package com.goride.dao;

import com.goride.models.RatingHandler;

import java.sql.SQLException;

public interface IRatingHandlerDAO {
   void setRating(RatingHandler ratingHandler) throws SQLException;
   int getRating(int driverId) throws SQLException;
}

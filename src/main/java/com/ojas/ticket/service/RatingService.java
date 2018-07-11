package com.ojas.ticket.service;

import com.ojas.ticket.entity.Rating;

public interface RatingService {

	Rating save(Rating rating);

	Rating getRating(Long id);

}

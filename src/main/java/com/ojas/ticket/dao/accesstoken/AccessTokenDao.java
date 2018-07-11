package com.ojas.ticket.dao.accesstoken;

import com.ojas.ticket.dao.Dao;
import com.ojas.ticket.entity.AccessToken;

/**
 * @author Philip Washington Sorst <philip@sorst.net>
 */
public interface AccessTokenDao extends Dao<AccessToken, Long>
{
    AccessToken findByToken(String accessTokenString);
}

package com.wfahle.hlog.utils;

public class Entity {

/*
 * adapted from dxcc_lib.h - Country codes definitions
 *
 *   See www.hosenose.com/adif/adif.html
 *   alphadelta.chez.tiscali.fr/num-ra.html
 */

	public Entity(int code, String country, int obsolete, int arrl, String callsign, int continent,
			String itu, String cq, int image) {
		Code = code;
		Country = country;
		Obsolete = obsolete;
		Arrl = arrl;
		Callsign = "("+callsign+")(.*)";
		Continent = continent;
		Itu = itu;
		Cq = cq;
		Image = image;
	}

    public int         Code ;
    public String		Country ;
    public int         Obsolete ;
    public int         Arrl ; /* = -1 when same as code . */
    public String		Callsign ;
    public int         Continent ;
    public String		Itu ;
    public String		Cq ;
    public int			Image;
}

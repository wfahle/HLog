package com.wfahle.hlog.utils;

import java.util.HashMap;

import com.wfahle.hlog.R;

public class GlobalDxccList {
	// We make a OR with continents, because some countries belong
	// // to two. Ex Turkey between Europe and Asia.
	public static final int ZZ=0x00; /* Not defined */
	public static final int EU=0x01; /* Europe. */
	public static final int NA=0x02; /* North-America */
	public static final int AS=0x04; /* Asia */
	public static final int AF=0x08; /* Africa */
	public static final int OC=0x10; /* Oceania */
	public static final int AN=0x11; /* Antartica */
	public static final int SA=0x12; /* South-America */
	/* Simple function that returns all information about a callsign. */
	public static Entity dxcc_display( String theCall ){

		int entity = -1;
		if (exceptions.containsKey(theCall)) {
			entity = exceptions.get(theCall);
		}
		Entity[] dxcc = GlobalDxccList.get_list();
		for( int myI = 0; myI < dxcc.length ; ++myI ) {
			Entity myD = dxcc[myI];
			if (entity != -1) { // if the callsign is in the exception list
				if (myD.Code == entity)
					return myD;
			} else { // look at the callsign vs. the regexes in the entity list
				if (myD.Callsign == null)
					continue;
				if (theCall.matches(myD.Callsign)) {
					return myD;
				}
			}
		  }

		return null;
	}

	private static final HashMap<String, Integer> exceptions; 
	static
	{
		exceptions = new HashMap<String, Integer>();
		// Antarctica: 13
		exceptions.put("VP8SIG", 13);
		exceptions.put("KC4AAA", 13);
		exceptions.put("RI1ANC", 13);
		exceptions.put("RI1ANT", 13);
		exceptions.put("RI20ANT", 13);

		// Christmas Island: 35
		exceptions.put("VK9EX", 35);
		
		// Clipperton Island: 36
		exceptions.put("TX5K", 36);

		// Cocos-Keeling Island: 38
		exceptions.put("VK9EC", 38);

		// French Guiana: 63
		exceptions.put("TO2A", 63);
		exceptions.put("TO7C", 63);
		exceptions.put("TO7IR", 63);

		// Guadeloupe: 79
		exceptions.put("TO0MT", 79);
		exceptions.put("TO1USB", 79);
		exceptions.put("TO1T", 79);
		exceptions.put("TO2FG", 79);
		exceptions.put("TO4T", 79);
		exceptions.put("TO4D", 79);
		exceptions.put("TO5BG", 79);
		exceptions.put("TO7T", 79);
		exceptions.put("TO9T", 79);
		
		// Martinique: 84
		exceptions.put("TO0O", 84);
		exceptions.put("TO1BT", 84);
		exceptions.put("TO1N", 84);
		exceptions.put("TO3GA", 84);
		exceptions.put("TO3JA", 84);
		exceptions.put("TO3T", 84);
		exceptions.put("TO3W", 84);
		exceptions.put("TO6M", 84);
		exceptions.put("TO7A", 84);
		exceptions.put("TO8A", 84);
		exceptions.put("TO8Z", 84);
		exceptions.put("TO9A", 84);
		exceptions.put("TO9R", 84);
		
		// St. Lucia: 97
		exceptions.put("TO4X",  97);
		
		// Guantanamo Bay: 105
		exceptions.put("KG4AI",  105);
		exceptions.put("KG4AJ",  105);
		exceptions.put("KG4AM",  105);
		exceptions.put("KG4AN",  105);
		exceptions.put("KG4AS",  105);
		exceptions.put("KG4AU",  105);
		exceptions.put("KG4BA",  105);
		exceptions.put("KG4BB",  105);
		exceptions.put("KG4CM",  105);
		exceptions.put("KG4CN",  105);
		exceptions.put("KG4CQ",  105);
		exceptions.put("KG4CT",  105);
		exceptions.put("KG4CW",  105);
		exceptions.put("KG4DP",  105);
		exceptions.put("KG4DX",  105);
		exceptions.put("KG4EM",  105);
		exceptions.put("KF4EME",  105);
		exceptions.put("KG4FD",  105);
		exceptions.put("KG4FY",  105);
		exceptions.put("KG4GJ",  105);
		exceptions.put("KG4HE",  105);
		exceptions.put("KG4HF",  105);
		exceptions.put("KG4JC",  105);
		exceptions.put("KG4JJ",  105);
		exceptions.put("KG4JR",  105);
		exceptions.put("KG4KD",  105);
		exceptions.put("KG4KL",  105);
		exceptions.put("KG4LH",  105);
		exceptions.put("KG4LL",  105);
		exceptions.put("KG4LP",  105);
		exceptions.put("KG4ML",  105);
		exceptions.put("KG4MN",  105);
		exceptions.put("KG4OX",  105);
		exceptions.put("KG4PC",  105);
		exceptions.put("KG4PK",  105);
		exceptions.put("KG4PL",  105);
		exceptions.put("KG4PT",  105);
		exceptions.put("KG4QB",  105);
		exceptions.put("KG4QD",  105);
		exceptions.put("KG4QH",  105);
		exceptions.put("KG4QW",  105);
		exceptions.put("KG4RB",  105);
		exceptions.put("KG4RR",  105);
		exceptions.put("KG4RX",  105);
		exceptions.put("KG4SB",  105);
		exceptions.put("KG4SS",  105);
		exceptions.put("KG4TJ",  105);
		exceptions.put("KG4TO",  105);
		exceptions.put("KG4VL",  105);
		exceptions.put("KG4VN",  105);
		exceptions.put("KG4WB",  105);
		exceptions.put("KG4WD",  105);
		exceptions.put("KG4WV",  105);
		exceptions.put("KG4WW",  105);
		exceptions.put("KG4XE",  105);
		exceptions.put("KG4ZD",  105);
		exceptions.put("KG4ZE",  105);
		exceptions.put("KG4ZI",  105);
		exceptions.put("KG4ZZ",  105);

		// exceptions.put("KG4ZOI",  105); // no longer GB
		// exceptions.put("KG4MOZ",  105);
		
		// Heard Island: 111
		exceptions.put("VK0CW", 111);
		exceptions.put("VK0EK", 111);
		exceptions.put("VK0HM", 111);
		exceptions.put("VK0HI", 111);
		exceptions.put("VK0IR", 111);
		exceptions.put("VK0WR", 111);
		exceptions.put("VK1RA", 111);
		exceptions.put("VK1VU", 111);
		exceptions.put("VK1YG", 111);
		
		// Falkland Islands: 141
		exceptions.put("VP8SCC", 141);
		
		// Lord Howe Island: 147
		exceptions.put("VK0YQS", 147);
		exceptions.put("VK9DLX",  147);

		// Macquarie Island: 153
		exceptions.put("VK0TH", 153);
		exceptions.put("VK0KEV", 153);
		
		// Mayotte: 169
		exceptions.put("TO7BC", 169);
		exceptions.put("TO5NED", 169);
		exceptions.put("TO8MZ", 169);
		exceptions.put("TO7RJ", 169);
		exceptions.put("TO2M", 169);
		exceptions.put("TO4M", 169);
		
		//Minami-Torishima: 177
		exceptions.put("JD1BCK", 177);
		exceptions.put("JD1BND", 177);
		exceptions.put("JD1YAA", 177);
		exceptions.put("JA6GXK/JD1", 177);
		exceptions.put("JD1/JA6GXK", 177);
		exceptions.put("JD1/JD1BIC", 177);
		exceptions.put("JD1/JE6XPF", 177);
		exceptions.put("JD1/JF3CTR", 177);
		exceptions.put("JD1/JF7MTO", 177);
		exceptions.put("JD1/JF8HIQ", 177);
		exceptions.put("JD1/JG8NQJ", 177);
		exceptions.put("JD1/JH1EFP", 177);
		exceptions.put("JD1/JI2AMA", 177);
		exceptions.put("JD1/JK1PCN", 177);
		exceptions.put("JD1/JR8XXQ", 177);
		exceptions.put("JD1BIC/JD1", 177);
		exceptions.put("JD1BME", 177);
		exceptions.put("JD1BMM", 177);
		exceptions.put("JD1M/JI2AMA", 177);
		exceptions.put("JD1YBJ", 177);
		exceptions.put("JF3CTR/JD1", 177);
		exceptions.put("JF7MTO/JD1", 177);
		exceptions.put("JF8HIQ/JD1", 177);
		exceptions.put("JG8NQJ/JD1", 177);
		exceptions.put("JH1EFP/JD1", 177);
		exceptions.put("JI2AMA/JD1", 177);
		exceptions.put("JK1PCN/JD1", 177);
		exceptions.put("JR8XXQ/JD1", 177);
		
		// Japan
		exceptions.put("JD1BHH/6", 339);
		
		// Navassa: 182
		exceptions.put("K1P", 182);
		
		// Norfolk: 189
		exceptions.put("VK9DAC", 189);
		
		// Saint Martin: 213
		exceptions.put("TO4X", 213);
		
		// San Felix: 217
		exceptions.put("3G0X", 217);
		
		// France: 227
		exceptions.put("TO7TSE", 227);
		
		// Spratly Island: 247
		exceptions.put("9M4SLL", 247);
		
		// St. Pierre and Miquelon: 277
		exceptions.put("TO7I", 277);
		exceptions.put("TO2FP", 277);
		exceptions.put("TO2U", 277);
		exceptions.put("TO0DX", 277);

		// Reunion Island: 453
		exceptions.put("TO5M", 453);
		exceptions.put("TO2Z", 453);
		exceptions.put("TO3R", 453);
		exceptions.put("TO2R", 453);
		exceptions.put("TO4WW", 453);
		exceptions.put("TO4G", 453);
		exceptions.put("TO4E", 453);
		exceptions.put("TO7CC", 453);

		// Swains Island
		exceptions.put("K1ER/KH8", 515);
		exceptions.put("K8YSE/KH8", 515);
		exceptions.put("KH6BK/KH8", 515);
		
		
		// Saint Barthelemy: 516
		exceptions.put("TO3A", 516);
		exceptions.put("TO3X", 516);
		exceptions.put("TO7ZG", 516);
		exceptions.put("TO2D", 516);
		exceptions.put("TO8YY", 516);
	}
	
	private static Entity[] list = null;
	private static Entity[] get_list() {
		if (list == null) 
		{
		Entity[] ret = { 
				// Source: https://www.itu.int/online/mms/glad/cga_callsign.sh?lang=en
				// Source: http://qrz.com
				// Note: This first regex will match anything starting with C7, 4Y, or anything
				// starting with 4U except 4U_ITU and 4U_UN, where _ is 0-9
				// UN operations besides UN HQ and ITU HQ (including ICAO 4Y and WMO C7) are 
				// not eligible for DXCC. You can still work them, though; they are legal.
				// Note: Mt. Athos must come before Greece, a few others
				// are order dependent. Probably a few other bugs in there around island territories
				// ss is not exactly right, but no one uses it, probably for that reason (Egypt/Sudan)
	new Entity(   0, "UNITED NATIONS", 0, 0, "^4U[0-9](?:(?!ITU))(?:(?!UN))|^4Y|^C7", ZZ, "ZZ", "ZZ", R.drawable.uno),
    new Entity(   1, "CANADA", 0, -1, "^V[A-GOXY]|^C[F-KZ]|^X[J-O]|^CY[1-8]", NA, "(H)", "1,2,3,4,5", R.drawable.ca ),
    new Entity(   2, "ABU AIL ISLAND", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.sa ),
    new Entity(   3, "AFGHANISTAN", 0, -1, "^YA|^T6", AS, "40", "21", R.drawable.af ),
    new Entity(   4, "AGALEGA & ST BRANDON", 0, -1, "^3B6|^3B7", AF, "53", "39", R.drawable.mu),
    new Entity(   5, "ALAND ISLAND", 0, -1, "^O[FGI]0|^OH0[^M]", EU, "18", "15", R.drawable.ax ),
    new Entity(   6, "ALASKA", 0, -1, "^[AKNW]L([0-8]|9[^K])|^[AKNW].*/[AKNW]L[0-9]$", NA, "1,2", "01", R.drawable.use ),
    new Entity(   7, "ALBANIA", 0, -1, "^ZA", EU, "37", "14", R.drawable.al ),
    new Entity(   8, "ALDABRA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(   9, "AMERICAN SAMOA", 0, -1, "^[AKNW]H8[^S]", OC, "62", "32", R.drawable.as ),
    new Entity(  10, "AMSTERDAM & ST PAUL", 0, -1, "^FT[5-8]Z", AF, "68", "39", R.drawable.tf ),
    new Entity(  11, "ANDAMAN & NICOBAR ISLAND", 0, -1, "^V[T-W]4|^8[T-Y]4|^A[T-W]4", AS, "49", "26", R.drawable.in ),
    new Entity(  12, "ANGUILLA", 0, -1, "^VP2E", NA, "11", "08", R.drawable.ai ),
    new Entity(  13, "ANTARCTICA", 0, -1, "^VK0[^M]", AN, "(B)", "(C)", R.drawable.aq),
    new Entity(  14, "ARMENIA", 0, -1, "^EK", AS, "29", "21", R.drawable.am ),
    new Entity(  15, "ASIATIC RUSSIA", 0, -1, "^[RU][890]|^R[A-Z][890]|^U[A-I][890]", AS, "(F)", "(G)", R.drawable.ru ),
    new Entity(  16, "AUCKLAND & CAMPBELL", 0, -1, "^ZL9", OC, "60", "32", R.drawable.nz ),
    new Entity(  17, "AVES ISLAND", 0, -1, "^Y[V-Y]0|^4M0", NA, "11", "08", R.drawable.ve ),
    new Entity(  18, "AZERBAIJAN", 0, -1, "^4J|^4K", AS, "29", "21", R.drawable.az ),
    new Entity(  19, "BAJO NUEVO", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  20, "BAKER, HOWLAND ISLAND", 0, -1, "^[AKNW]H1|^[ANKW].*/KH1$", OC, "64", "27", R.drawable.use ),
    new Entity(  21, "BALEARIC ISLAND", 0, -1, "^E[A-H]6|^A[M-O]6", EU, "37", "14", R.drawable.es ),
    new Entity(  22, "BELAU (PALAU)", 0, -1, "^T8", OC, "64", "27", R.drawable.pw ),
    new Entity(  23, "BLENHEIM REEF", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  24, "BOUVET", 0, -1, "^3Y.*/B", AF, "67", "38", R.drawable.bv ),
    new Entity(  25, "BRITISH N. BORENO", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  26, "BRITISH SOMALI", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  27, "BELARUS", 0, -1, "^E[U-W]", EU, "29", "16", R.drawable.by ),
    new Entity(  28, "CANAL ZONE", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  29, "CANARY ISLAND", 0, -1, "^E[A-H]8|^A[M-O]8", AF, "36", "33", R.drawable.es ),
    new Entity(  30, "CELEBE/MOLUCCA ISLAND", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  31, "CENTRAL KIRIBATI", 0, -1, "^T31", OC, "61", "31", R.drawable.ki ),
    new Entity(  32, "CEUTA & MELILLA", 0, -1, "^E[A-H]9|^A[M-O]9", AF, "37", "33", R.drawable.es ),
    new Entity(  33, "CHAGOS", 0, -1, "^VQ9", AF, "41", "39", R.drawable.io ),
    new Entity(  34, "CHATHAM ISLAND", 0, -1, "^ZL7", OC, "60", "32", R.drawable.nz ),
    new Entity(  35, "CHRISTMAS ISLAND", 0, -1, "^VK9X", NA, "11", "08", R.drawable.cx ),
    new Entity(  36, "CLIPPERTON ISLAND", 0, -1, "^FO.*/C", NA, "10", "07", R.drawable.tf ),
    new Entity(  37, "COCOS ISLAND", 0, -1, "^TI9", NA, "11", "07", R.drawable.cc ),
    new Entity(  38, "COCOS-KEELING ISLAND", 0, -1, "^VK9C", OC, "54", "29", R.drawable.cc ),
    new Entity(  39, "COMOROS (FB8)", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  40, "CRETE", 0, -1, "^S[V-Z]9|^S[V-Z].*/9", EU, "28", "20", R.drawable.gr ),
    new Entity(  41, "CROZET", 0, -1, "^FT[5-8]W", AF, "68", "39", R.drawable.tf ),
    new Entity(  42, "DAMAO, DIU", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  43, "DESECHEO ISLAND", 0, -1, "^[KNW]P5|^[ANKW].*/KP5$", NA, "11", "08", R.drawable.use ),
    new Entity(  44, "DESROCHES", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  45, "DODECANESE", 0, -1, "^S[V-Z]5|^S[V-Z].*/5", EU, "28", "20", R.drawable.gr ),
    new Entity(  46, "EAST MALAYSIA", 0, -1, "^9[MW]6|^9[MW]8", OC, "54", "28", R.drawable.my ), 
    new Entity(  47, "EASTER ISLAND", 0, -1, "^C[A-E]0[A-WY]|^3G0[A-WY]|^X[QR]0[A-WY]", SA, "63", "12", R.drawable.cl ),
    new Entity(  48, "EASTERN KIRIBATI", 0, -1, "^T32", OC, "61", "31", R.drawable.ki ),
    new Entity(  49, "EQUATORIAL GUINEA", 0, -1, "^3C[^0]", AF, "47", "36", R.drawable.gq ),
    new Entity(  50, "MEXICO", 0, -1, "^X[A-I]|^4[ABC]|^6[D-J]", NA, "10", "06", R.drawable.mx ),
    new Entity(  51, "ERITREA", 0, -1, "^E3", AF, "48", "37", R.drawable.er ), 
    new Entity(  52, "ESTONIA", 0, -1, "^ES", EU, "29", "15", R.drawable.ee ),
    new Entity(  53, "ETHIOPIA", 0, -1, "^ET|^9[EF]", AF, "48", "37", R.drawable.et ),
    new Entity(  61, "FRANZ JOSEF LAND", 0, -1, "^R1FJ", EU, "75", "40", R.drawable.ru ),
    new Entity(  54, "EUROPEAN RUSSIA", 0, -1, "^RA[13-7/]|^R[B-Z][1-7]|^[RU][1-7]|^U[A-I][13-7]", EU, "(E)", "16", R.drawable.ru ),
    new Entity(  55, "FARQUHAR", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  56, "FERNANDO DE NORONHA", 0, -1, "^P[P-Y]0[FRMZ]", SA, "13", "11", R.drawable.br ),
    new Entity(  57, "FRENCH EQ. AFRICA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  58, "FRENCH INDO-CHINA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  59, "FRENCH WEST AFRICA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  60, "BAHAMAS", 0, -1, "^C6", NA, "11", "08", R.drawable.bs ),
    new Entity(  62, "BARBADOS", 0, -1, "^8P", NA, "11", "08", R.drawable.bb ),
    new Entity(  63, "FRENCH GUIANA", 0, -1, "^FY", SA, "12", "09", R.drawable.gf ),
    new Entity(  64, "BERMUDA", 0, -1, "^VP9", NA, "11", "05", R.drawable.bm ),
    new Entity(  65, "BRITISH VIRGIN ISLAND", 0, -1, "^VP2V", NA, "11", "08", R.drawable.vg ),
    new Entity(  66, "BELIZE", 0, -1, "^V3", NA, "11", "07", R.drawable.bz ),
    new Entity(  67, "FRENCH INDIA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  68, "SAUDI/KUWAIT N.Z.", 1, -1, null, AS, "39", "21", R.drawable.zz ),
    new Entity(  69, "CAYMAN ISLANDS", 0, -1, "^ZF", NA, "11", "08", R.drawable.ky ),
    new Entity(  70, "CUBA", 0, -1, "^C[LMO]|^T4", NA, "11", "08", R.drawable.cu ),
    new Entity(  71, "GALAPAGOS", 0, -1, "^H[CD]8", SA, "12", "10", R.drawable.ec ),
    new Entity(  72, "DOMINICAN REPUBLIC", 0, -1, "^HI", NA, "11", "08", R.drawable.dr ),
    new Entity(  74, "EL SALVADOR", 0, -1, "^YS|^HU", NA, "11", "07", R.drawable.sv ),
    new Entity(  75, "GEORGIA", 0, -1, "^4L", AS, "29", "21", R.drawable.ge ),
    new Entity(  76, "GUATEMALA", 0, -1, "^TD|^TG", NA, "11", "07", R.drawable.gt ),
    new Entity(  77, "GRENADA", 0, -1, "^J3", NA, "11", "08", R.drawable.gd ),
    new Entity(  78, "HAITI", 0, -1, "^HH|^4V", NA, "11", "08", R.drawable.ht ),
    new Entity(  79, "GUADELOUPE", 0, -1, "^FG|^TO6[^M]|^TO8[^AZMY]|^TO2[^M]", NA, "11", "08", R.drawable.gp ),
    new Entity(  80, "HONDURAS", 0, -1, "^HQ|^HR", NA, "11", "07", R.drawable.hn ),
    new Entity(  81, "GERMANY", 1, -1, null, EU, "28", "14", R.drawable.de ),
    new Entity(  82, "JAMAICA", 0, -1, "^6Y", NA, "11", "08", R.drawable.jm ),
    new Entity(  84, "MARTINIQUE", 0, -1, "^FM|^TO5|^TO4[^M]", NA, "11", "08", R.drawable.mq ),
    new Entity(  85, "BONAIRE,CURACAO", 1, -1, null, SA, "11", "09", R.drawable.zz ),
    new Entity(  86, "NICARAGUA", 0, -1, "^YN|^H[67T]", NA, "11", "07", R.drawable.ni ),
    new Entity(  88, "PANAMA", 0, -1, "^H[OP389]|^3[EF]", NA, "11", "07", R.drawable.pa ),
    new Entity(  89, "TURKS & CAICOS ISLAND", 0, -1, "^VP5", NA, "11", "08", R.drawable.tc ),
    new Entity(  90, "TRINIDAD & TOBAGO", 0, -1, "^9Y|^9Z", SA, "11", "09", R.drawable.tt ),
    new Entity(  91, "ARUBA", 0, -1, "^P4|^PJ3", SA, "11", "09", R.drawable.aw ),
    new Entity(  93, "GEYSER REEF", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity(  94, "ANTIGUA & BARBUDA", 0, -1, "^V2", NA, "11", "08", R.drawable.ag ),
    new Entity(  95, "DOMINICA", 0, -1, "^J7", NA, "11", "08", R.drawable.dm ),
    new Entity(  96, "MONTSERRAT", 0, -1, "^VP2M", NA, "11", "08", R.drawable.ms ),
    new Entity(  97, "ST LUCIA", 0, -1, "^J6", NA, "11", "08", R.drawable.lc ),
    new Entity(  98, "ST VINCENT", 0, -1, "^J8", NA, "11", "08", R.drawable.vc ),
    new Entity(  99, "GLORIOSO ISLAND", 0, -1, "^FR.*G", AF, "53", "39", R.drawable.tf ),
    new Entity( 100, "ARGENTINA", 0, -1, "^L[O-W]|^A[YZ]|^L[2-9]", SA, "14-16", "13", R.drawable.ar ),
    new Entity( 101, "GOA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 102, "GOLD COAST/TOGOLND", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 103, "GUAM", 0, -1, "^[AKNW]H2", OC, "64", "27", R.drawable.gu ),
    new Entity( 104, "BOLIVIA", 0, -1, "^CP", SA, "12", "10", R.drawable.bo ),
    new Entity( 105, "GUANTANAMO BAY", 0, -1, "^KG4/|^[ANKW].*/KG4$", NA, "11", "08", R.drawable.use ),
    new Entity( 106, "GUERNSEY", 0, -1, "^[GM2][PU]", EU, "27", "14", R.drawable.gg ),
    new Entity( 107, "REPUBLIC OF GUINEA", 0, -1, "^3X", AF, "46", "35", R.drawable.gn ),
    new Entity( 108, "BRAZIL", 0, -1, "^P[P-Y][^0]|^Z[V-Z]|^P[P-Y]0[^FRSTZM]", SA, "(D)", "11", R.drawable.br ),
    new Entity( 109, "GUINEA-BISSAU", 0, -1, "^J5", AF, "46", "35", R.drawable.gw ),
    new Entity( 110, "HAWAII", 0, -1, "^[AKNW]H6|^[AKNW]H7[^K]|^[AKNW].*/KH6", OC, "61", "31", R.drawable.use ),
    new Entity( 111, "HEARD ISLAND", 0, -1, "^VK0.*/H", AF, "68", "39", R.drawable.aq ),
    new Entity( 112, "CHILE", 0, -1, "^C[A-E][^09]|^3G[^09]|^X[QR][^09]", SA, "14", "12", R.drawable.cl ),
    new Entity( 113, "IFNI", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 114, "ISLE OF MAN", 0, -1, "^[GM2][DT]", EU, "27", "14", R.drawable.im ),
    new Entity( 115, "ITALIAN SOMALI", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 116, "COLOMBIA", 0, -1, "^[H5][JK][^0]", SA, "12", "09", R.drawable.co ),
    new Entity( 117, "ITU GENEVA", 0, -1, "^4U[0-9]ITU", EU, "28", "14", R.drawable.uno ),
    new Entity( 118, "JAN MAYEN", 0, -1, "^JX", EU, "18", "40", R.drawable.sj ),
    new Entity( 119, "JAVA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 120, "ECUADOR", 0, -1, "^H[CD][^8]", SA, "12", "10", R.drawable.ec ),
    new Entity( 122, "JERSEY", 0, -1, "^[GM2][HJ]", EU, "27", "14", R.drawable.je ),
    new Entity( 123, "JOHNSTON ISLAND", 0, -1, "^[AKNW]H3|^[ANKW].*/KH3$", OC, "61", "31", R.drawable.use ), 
    new Entity( 124, "JUAN DE NOVA", 0, -1, "^FR.*/[EJ]", AF, "53", "39", R.drawable.tf ), 
    new Entity( 125, "JUAN FERNANDEZ", 0, -1, "^C[A-E]0Z|^3G0Z|^X[QR]0Z", SA, "14", "12", R.drawable.cl ),
    new Entity( 126, "KALININGRAD", 0, -1, "^U[A-I]2|^RA2", EU, "29", "15", R.drawable.ru ),
    new Entity( 127, "KAMARAN ISLAND", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 128, "KARELO-FINN REP", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 129, "GUYANA", 0, -1, "^8R", SA, "12", "09", R.drawable.gy ),
    new Entity( 130, "KAZAKHSTAN", 0, -1, "^U[N-Q]", AS, "29", "17" , R.drawable.kz),
    new Entity( 131, "KERGUELEN", 0, -1, "^FT[5-8]X|^FT.*/X", AF, "68", "39", R.drawable.tf ),
    new Entity( 132, "PARAGUAY", 0, -1, "^ZP", SA, "14", "11", R.drawable.py ),
    new Entity( 133, "KERMADEC", 0, -1, "^ZL8", OC, "60", "32", R.drawable.nz ),
    new Entity( 134, "KINGMAN REEF", 0, -1, "^[AKNW]H5K|^[ANKW].*/KH5K$", OC, "61", "31", R.drawable.use ), 
    new Entity( 135, "KYRGYZSTAN", 0, -1, "^EX", AS, "30,31", "17", R.drawable.kg ),
    new Entity( 136, "PERU", 0, -1, "^O[A-C]|^4T", SA, "12", "10", R.drawable.pe ), 
    new Entity( 137, "SOUTH KOREA", 0, -1, "^HL|^D[ST789]|^6[K-N]|^KL9K[A-H]", AS, "44", "25", R.drawable.kr ),
    new Entity( 138, "KURE ISLAND", 0, -1, "^[AKNW]H7K|^[ANKW].*/KH7K$", OC, "61", "31", R.drawable.use ),
    new Entity( 139, "KURIA MURIA ISLAND", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 140, "SURINAME", 0, -1, "^PZ", SA, "12", "09", R.drawable.sr ),
    new Entity( 141, "FALKLAND ISLAND", 0, -1, "^VP8[A-EKLNVY]", SA, "16", "13", R.drawable.fk ),
    new Entity( 142, "LAKSHADWEEP ISLANDS", 0, -1, "^V[T-W]7|^8[T-Y]7|^A[T-W]7", AS, "41", "22", R.drawable.in ),
    new Entity( 143, "LAOS", 0, -1, "^XW", AS, "49", "26", R.drawable.la),
    new Entity( 144, "URUGUAY", 0, -1, "^C[V-X]", SA, "14", "13", R.drawable.uy ),
    new Entity( 145, "LATVIA", 0, -1, "^YL", EU, "29", "15", R.drawable.lv ),
    new Entity( 146, "LITHUANIA", 0, -1, "^LY", EU, "29", "15", R.drawable.lt ),
    new Entity( 147, "LORD HOWE ISLAND", 0, -1, "^VK9[ELOPR]", OC, "60", "30", R.drawable.au ),
    new Entity( 148, "VENEZUELA", 0, -1, "^Y[V-Y][^0]|^4M[^0]", SA, "12", "09", R.drawable.ve ),
    new Entity( 149, "AZORES", 0, -1, "^CU|^CQ1|^CS4|^CR[12]|^C[QRST]8", EU, "36", "14", R.drawable.azi ),
    new Entity( 150, "AUSTRALIA", 0, -1, "^AX|^V[H-NZ][^09]", OC, "(I)", "29,30", R.drawable.au ),
    new Entity( 151, "MALYJ VYSOTSKI ISLAND", 1, -1, null, EU, "29", "16", R.drawable.ru ),
    new Entity( 152, "MACAO", 0, -1, "^XX", AS, "49", "26", R.drawable.mo ),
    new Entity( 153, "MACQUARIE ISLAND", 0, -1, "^VK0M", OC, "60", "30", R.drawable.au ),
    new Entity( 154, "YEMEN ARAB REP", 1, -1, null, AS, "39", "21", R.drawable.zz ),
    new Entity( 155, "MALAYA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 157, "NAURU", 0, -1, "^C2", OC, "65", "31", R.drawable.nr ),
    new Entity( 158, "VANUATU", 0, -1, "^YJ", OC, "56", "32", R.drawable.vu ),
    new Entity( 159, "MALDIVES ISLAND", 0, -1, "^8Q", AS | AF, "41", "22", R.drawable.mv ),
    new Entity( 160, "TONGA", 0, -1, "^A3", OC, "62", "32", R.drawable.to ),
    new Entity( 161, "MALPELO ISLAND", 0, -1, "^HK0[MNT]", SA, "12", "09", R.drawable.co ),
    new Entity( 162, "NEW CALEDONIA", 0, -1, "^FK|^TX1|^TX8", OC, "56", "32", R.drawable.nc ),
    new Entity( 163, "PAPUA NEW GUINEA", 0, -1, "^P2", OC, "51", "28", R.drawable.pg ),
    new Entity( 164, "MANCHURIA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 165, "MAURITIUS ISLAND", 0, -1, "^3B8", AF, "53", "39", R.drawable.mu ),
    new Entity( 166, "MARIANA ISLAND", 0, -1, "^[AKNW]H0", OC, "64", "27", R.drawable.mp ),
    new Entity( 167, "MARKET REEF", 0, -1, "^OH0M|^OJ0", EU, "18", "15", R.drawable.fi ),
    new Entity( 168, "MARSHALL ISLAND", 0, -1, "^V7", OC, "65", "31", R.drawable.mh ),
    new Entity( 169, "MAYOTTE", 0, -1, "^FH", AF, "53", "39", R.drawable.yt ),
    new Entity( 170, "NEW ZEALAND", 0, -1, "^ZL[^789]|^ZK[^0123]|^ZM", OC, "60", "32", R.drawable.nz ),
    new Entity( 171, "MELLISH REEF", 0, -1, "^VK9[GMW]", OC, "56", "30", R.drawable.au ),
    new Entity( 172, "PITCAIRN ISLAND", 0, -1, "^VP6", OC, "63", "32", R.drawable.pn ),
    new Entity( 173, "MICRONESIA", 0, -1, "^V6", OC, "65", "27", R.drawable.fm ),
    new Entity( 174, "MIDWAY ISLAND", 0, -1, "^[AKNW]H4|^[ANKW].*/KH4$", OC, "61", "31", R.drawable.use ),
    new Entity( 175, "FRENCH POLYNESIA", 0, -1, "^FO|^TX4", OC, "63", "32", R.drawable.pf ),
    new Entity( 176, "FIJI", 0, -1, "^3D2|^3D[N-Z]", OC, "56", "32", R.drawable.fj ),
    new Entity( 177, "MINAMI TORISHIMA", 0, -1, "^JD1.*/M", OC, "90", "27", R.drawable.jp ),
    new Entity( 178, "MINERVA REEF", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 179, "MOLDOVA", 0, -1, "^ER", EU, "29", "16", R.drawable.md ),
    new Entity( 180, "MT ATHOS (SY)", 0, -1, "^SV.*/A", EU, "28", "20", R.drawable.gr ), 
    new Entity( 181, "MOZAMBIQUE", 0, -1, "^C8|^C9", AF, "53", "37", R.drawable.mz ),
    new Entity( 182, "NAVASSA ISLAND", 0, -1, "^[KNW]P1|^[ANKW].*/KP1$", NA, "11", "08", R.drawable.use ),
    new Entity( 183, "DUTCH BORNEO", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 184, "NETHER N. GUNIEA", 1, -1, null, ZZ, "ZZ", "ZZ" , R.drawable.zz),
    new Entity( 185, "SOLOMON ISLANDS", 0, -1, "^H4[^0]", OC, "51", "29", R.drawable.sb ),
    new Entity( 186, "NEWFOUNDLAND/LAB", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 187, "NIGER", 0, -1, "^5U", AF, "46", "35", R.drawable.ne ),
    new Entity( 188, "NIUE", 0, -1, "^ZK2|^E6", OC, "62", "32", R.drawable.nz ),
    new Entity( 189, "NORFOLK ISLAND", 0, -1, "^VK9[AFJN0]", OC, "60", "32", R.drawable.nf ),
    new Entity( 190, "SAMOA", 0, -1, "^5W", OC, "62", "32", R.drawable.ws ),
    new Entity( 191, "NORTHERN COOK ISLAND", 0, -1, "^ZK1", OC, "62", "32", R.drawable.ck ),
    new Entity( 192, "OGASAWARA", 0, -1, "^JD1", AS, "45", "27", R.drawable.jp ),
    new Entity( 193, "OKINAWA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 194, "OKINO TORI-SHIMA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 195, "PAGALU (ANNOBAR ISLAND)", 0, -1, "^3C0", AF, "52", "36", R.drawable.gq ),
    new Entity( 196, "PALESTINE (ZC6)", 1, -1, null, AS, "39", "20", R.drawable.zz ),
    new Entity( 197, "PALMYRA & JARVIS ISLAND", 0, -1, "^[AKNW]H5[^K]", OC, "61,62", "31", R.drawable.use ),
    new Entity( 198, "PAPUA TERR", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 199, "PETER I ISLAND", 0, -1, "^3Y", AN, "72", "12", R.drawable.no ),
    new Entity( 200, "PORTUGUESE TIMOR", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 201, "PRINCE EDWARD & MARION", 0, -1, "^ZS8", AF, "57", "38", R.drawable.za ),
    new Entity( 202, "PUERTO RICO", 0, -1, "^[KNW]P[3-4]|^[ANKW].*/KP4", NA, "11", "08", R.drawable.pr ),
    new Entity( 203, "ANDORRA", 0, -1, "^C3", EU, "27", "14", R.drawable.ad ),
    new Entity( 204, "REVILLAGIGEDO ISLANDS", 0, -1, "^X[A-I]4", NA, "10", "06", R.drawable.mx ),
    new Entity( 205, "ASCENSION ISLAND", 0, -1, "^ZD8", AF, "66", "36", R.drawable.sh ),
    new Entity( 206, "AUSTRIA", 0, -1, "^OE", EU, "28", "15", R.drawable.at ),
    new Entity( 207, "RODRIGUEZ ISLAND", 0, -1, "^3B9", AF, "53", "39", R.drawable.mu ),
    new Entity( 208, "RUANDA-URUNDI", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 209, "BELGIUM", 0, -1, "^O[N-T]", EU, "27", "14", R.drawable.be ),
    new Entity( 210, "SAAR", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 211, "SABLE ISLAND", 0, -1, "^CY0", NA, "09", "05", R.drawable.ca ), 
    new Entity( 212, "BULGARIA", 0, -1, "^LZ", EU, "28", "20", R.drawable.bg ), 
    new Entity( 213, "SAINT MARTIN", 0, -1, "^FJ|^FS", NA, "11", "08", R.drawable.mf ), 
    new Entity( 214, "CORSICA", 0, -1, "^TK", EU, "28", "15", R.drawable.fr ),
    new Entity( 215, "CYPRUS", 0, -1, "^5B|^C4|^H2|^P3|^1B", AS, "39", "20", R.drawable.cy ),
    new Entity( 216, "SAN ANDREAS & PROVIDENCIA", 0, -1, "^HK0[^MNT]", NA, "11", "09", R.drawable.co ),
    new Entity( 217, "SAN FELIX", 0, -1, "^C[A-E]0X|^X[QR]0X", SA, "14", "12", R.drawable.cl ),
    new Entity( 218, "CZECHOSLOVAKIA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 219, "SAO TOME & PRINCIPE", 0, -1, "^S9", AF, "47", "36", R.drawable.st ),
    new Entity( 220, "SARAWAK", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 221, "DENMARK", 0, -1, "^O[UVZ]|^5[PQ]", EU, "18", "14", R.drawable.dk ),
    new Entity( 222, "FAROE ISLAND", 0, -1, "^O[WY]", EU, "18", "14", R.drawable.fo ), 
    new Entity( 223, "ENGLAND", 0, -1, "^[GM2][^CDHIJMNPSTUW]|^Z[NO]", EU, "27", "14", R.drawable.gb ),
    new Entity( 224, "FINLAND", 0, -1, "^O[F-J][^0]", EU, "18", "15", R.drawable.fi ),
    new Entity( 225, "SARDINIA", 0, -1, "^IS", EU, "28", "15", R.drawable.it ),
    new Entity( 226, "SAUDI/IRAQ N.Z.", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 227, "FRANCE", 0, -1, "^F[^GHJKMOPRSTWY]|^T[HMPQV]|^H[W-Y]", EU, "27", "14", R.drawable.fr ),
    new Entity( 228, "SERRANA BANK", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 229, "GERMAN DEM. REP.", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 230, "FED REP OF GERMANY", 0, -1, "^D[A-R]|^Y[2-9]", EU, "28", "14", R.drawable.de ),
    new Entity( 231, "SIKKIM", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 232, "SOMALIA", 0, -1, "^T5|^6O", AF, "48", "37", R.drawable.so ),
    new Entity( 233, "GIBRALTAR", 0, -1, "^ZB|^ZQ", EU, "37", "14", R.drawable.gi ),
    new Entity( 234, "SOUTHERN COOK ISLAND", 0, -1, "^ZK0|^E5", OC, "62", "32", R.drawable.ck ), 
    new Entity( 235, "SOUTH GEORGIA ISLAND", 0, -1, "^VP8G|^VP8.*/G", SA, "73", "13", R.drawable.gs ), 
    new Entity( 236, "GREECE", 0, -1, "^S[V-Z][^59]|^J4", EU, "28", "20", R.drawable.gr ), 
    new Entity( 237, "GREENLAND", 0, -1, "^OX|^XP", NA, "5,75", "40", R.drawable.gl ),
    new Entity( 238, "SOUTH ORKNEY ISLAND", 0, -1, "^VP8O|^VP8.*/O", SA, "73", "13", R.drawable.aq ),
    new Entity( 239, "HUNGARY", 0, -1, "^HA|^HG", EU, "28", "15", R.drawable.hu ), 
    new Entity( 240, "SOUTH SANDWICH ISLANDS", 0, -1, "^VP8SSI|^VP8.*/S", SA, "73", "13", R.drawable.gs ),
    new Entity( 241, "SOUTH SHETLAND ISLANDS", 0, -1, "^4K1|^CE9|^VP8.*/H|^VP8H|^RI1ANU|^RI44", SA, "73", "13", R.drawable.aq ), 
    new Entity( 242, "ICELAND", 0, -1, "^TF", EU, "17", "40", R.drawable.is ),
    new Entity( 243, "DEM REP OF YEMEN", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 244, "SOUTHERN SUDAN", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 245, "IRELAND", 0, -1, "^EI|^EJ", EU, "27", "14", R.drawable.ie ),
    new Entity( 246, "MALTA, SOVERIGN", 0, -1, "^1A0", EU, "28", "15", R.drawable.dk ),
    new Entity( 247, "SPRATLY ISLAND", 0, -1, "^1S", AS, "50", "26", R.drawable.my ),
    new Entity( 248, "ITALY", 0, -1, "^I[^S]", EU, "28,37,15", "33", R.drawable.it ),
    new Entity( 249, "ST KITTS & NEVIS", 0, -1, "^V4", NA, "11", "08", R.drawable.kn ),
    new Entity( 250, "ST HELENA ISLAND", 0, -1, "^ZD7", AF, "66", "36", R.drawable.sh ),
    new Entity( 251, "LIECHTENSTEIN", 0, -1, "^H[EB]0", EU, "28", "14", R.drawable.li ),
    new Entity( 252, "ST PAUL ISLAND", 0, -1, "^CY9", NA, "09", "05", R.drawable.ca ),
    new Entity( 253, "ST PETER & ST PAUL ROCKS", 0, -1, "^P[P-Y]0S", SA, "13", "11", R.drawable.br ),
    new Entity( 254, "LUXEMBOURG", 0, -1, "^LX", EU, "27", "14", R.drawable.lu ),
    new Entity( 255, "ST MAARTEN, SABA AND ST. EUSTATIUS", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 256, "MADEIRA ISLAND", 0, -1, "^C[RST][39]|^CQ[239]", AF, "36", "33", R.drawable.mdi ),
    new Entity( 257, "MALTA", 0, -1, "^9H", EU, "28", "15", R.drawable.mt ),
    new Entity( 258, "SUMATRA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 259, "SVALBARD ISLAND", 0, -1, "^JW", EU, "18", "40", R.drawable.sj ),
    new Entity( 260, "MONACO", 0, -1, "^3A", EU, "27", "14", R.drawable.mc ),
    new Entity( 261, "SWAN ISLAND", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 262, "TAJIKISTAN", 0, -1, "^EY", AS, "30", "17", R.drawable.tj ),
    new Entity( 263, "NETHERLANDS", 0, -1, "^P[A-I]", EU, "27", "14", R.drawable.nl ),
    new Entity( 264, "TANGIER", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 265, "NORTHERN IRELAND", 0, -1, "^[GM2][IN]", EU, "27", "14", R.drawable.gb ),
    new Entity( 266, "NORWAY", 0, -1, "^L[A-N]|^3Y|J[WX]", EU, "18", "14", R.drawable.no ),
    new Entity( 267, "TERR NEW GUINEA", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 268, "TIBET", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 269, "POLAND", 0, -1, "^S[N-R]|^HF|^3Z", EU, "28", "15", R.drawable.pl ),
    new Entity( 270, "TOKELAU ISLAND", 0, -1, "^ZK3", OC, "62", "31", R.drawable.tk ),
    new Entity( 271, "TRIESTE", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 272, "PORTUGAL", 0, -1, "^C[Q-T]7|^CT[01245]|^C[QR][0456]|^CS[056]", EU, "37", "14", R.drawable.pt ),
    new Entity( 273, "TRINDADE & MARTIN VAZ", 0, -1, "^P[P-Y]0T", SA, "15", "11", R.drawable.br ),
    new Entity( 274, "TRISTAN DA CUNHA", 0, -1, "^ZD9", AF, "66", "38", R.drawable.sh ),
    new Entity( 275, "ROMANIA", 0, -1, "^Y[O-R]", EU, "28", "20", R.drawable.ro ), 
    new Entity( 276, "TROMELIN", 0, -1, "^FR.*/T|^FT4", AF, "53", "39", R.drawable.tf ),
    new Entity( 277, "ST PIERRE & MIQUELON", 0, -1, "^FP", NA, "09", "05", R.drawable.pm ),
    new Entity( 278, "SAN MARINO", 0, -1, "^T7", EU, "28", "15", R.drawable.sm ),
    new Entity( 279, "SCOTLAND", 0, -1, "^[GM2][MS]", EU, "27", "14", R.drawable.scotland ),
    new Entity( 280, "TURKMENISTAN", 0, -1, "^EZ", AS, "30", "17", R.drawable.tm ), 
    new Entity( 281, "SPAIN", 0, -1, "^E[A-H][^689]|^A[M-O][^689]", EU, "37", "14", R.drawable.es ), 
    new Entity( 282, "TUVALU", 0, -1, "^T2", OC, "65", "31", R.drawable.tv ), 
    new Entity( 283, "UK BASES ON CYPRUS", 0, -1, "^ZC", AS, "39", "20", R.drawable.gb ), 
    new Entity( 284, "SWEDEN", 0, -1, "^S[A-M]|^7S|^8S", EU, "18", "14", R.drawable.se ),
    new Entity( 285, "VIRGIN ISLANDS", 0, -1, "^[KNW]P2|^[AKNW].*/KP2", NA, "11", "08", R.drawable.vi ),
    new Entity( 286, "UGANDA", 0, -1, "^5X", AF, "48", "37", R.drawable.ug ),
    new Entity( 287, "SWITZERLAND", 0, -1, "^H[EB][^0]", EU, "28", "14", R.drawable.ch ),
    new Entity( 288, "UKRAINE", 0, -1, "^E[M-O]|^U[R-Z]", EU, "29", "16", R.drawable.ua ),
    new Entity( 289, "HQ UNITED NATIONS", 0, -1, "^4U[0-9]UN", NA, "08", "05", R.drawable.uno ), 
    new Entity( 291, "UNITED STATES", 0, -1, "^A[A-GI-K]|^[KNW][^H]", NA, "6,7,8", "3,4,5", R.drawable.us ), 
    new Entity( 292, "UZBEKISTAN", 0, -1, "^U[J-M]", AS, "30", "17", R.drawable.uz ), 
    new Entity( 293, "VIETNAM", 0, -1, "^3W|^XV", AS, "49", "26", R.drawable.vn ),
    new Entity( 294, "WALES", 0, -1, "^[GM2][CW]", EU, "27", "14", R.drawable.wales ),
    new Entity( 295, "VATICAN", 0, -1, "^HV", EU, "28", "15", R.drawable.va ),
    new Entity( 296, "SERBIA", 0, -1, "^YT|^YU|^YZ", EU, "28", "15", R.drawable.rs ),
    new Entity( 297, "WAKE ISLAND", 0, -1, "^[AKNW]H9|^[ANKW].*/KH9$", OC, "65", "31", R.drawable.use ),
    new Entity( 298, "WALLIS & FUTUNA", 0, -1, "^FW|^TW", OC, "62", "32", R.drawable.wf ),
    new Entity( 299, "WESTERN MALAYSIA", 0, -1, "^9[MW]2|^9[MW]4", AS, "54", "28", R.drawable.my ),
    new Entity( 301, "WESTERN KIRIBATI", 0, -1, "^T30", OC, "65", "31", R.drawable.ki ),
    new Entity( 302, "WESTERN SAHARA", 0, -1, "^S0", AS, "41", "22", R.drawable.eh ),
    new Entity( 303, "WILLIS ISLAND", 0, -1, "^VK9[DW]", OC, "55", "30", R.drawable.au ),
    new Entity( 304, "BAHRAIN", 0, -1, "^A9", AS, "39", "21", R.drawable.bh ),
    new Entity( 305, "BANGLADESH", 0, -1, "^S[23]", AS, "41", "22", R.drawable.bd ),
    new Entity( 306, "BHUTAN", 0, -1, "^A5", AS, "41", "22", R.drawable.bt ),
    new Entity( 307, "ZANZIBAR", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 308, "COSTA RICA", 0, -1, "^TE|^TI", NA, "11", "07", R.drawable.cr ),
    new Entity( 309, "MYANMAR", 0, -1, "^XY|^XZ", AS, "49", "26", R.drawable.mm ),
    new Entity( 312, "KAMPUCHEA (CAMBODIA)", 0, -1, "^XU", AS, "49", "26", R.drawable.kh ),
    new Entity( 315, "SRI LANKA", 0, -1, "^4[P-S]", AS, "41", "22", R.drawable.lk ),
    new Entity( 318, "CHINA", 0, -1, "^B[0-9A-LRTYZ]|^3[H-U]|^XS|^BS[^7]", AS, "(A)", "23,24", R.drawable.cn ),
    new Entity( 321, "HONG KONG", 0, -1, "^VR|^VS", AS, "44", "24", R.drawable.hk ),
    new Entity( 324, "INDIA", 0, -1, "^V[T-W][^47]|^8[T-Y][^47]|^A[T-W][^47]", AS, "41", "22", R.drawable.in ),
    new Entity( 327, "INDONESIA", 0, -1, "^Y[B-H]|^7[A-I]|^8[A-I]|^JZ|^P[K-O]", OC, "51", "28", R.drawable.id ), 
    new Entity( 330, "IRAN", 0, -1, "^EP|^EQ|^9[B-D]", AS, "40", "21", R.drawable.ir ),
    new Entity( 333, "IRAQ", 0, -1, "^YI|^HN", AS, "39", "21", R.drawable.iq ),
    new Entity( 336, "ISRAEL", 0, -1, "^4X|^4Z", AS, "39", "20", R.drawable.il),
    new Entity( 339, "JAPAN", 0, -1, "^J[A-CE-S]|^7[J-N]|^8[J-N]", AS, "45", "25", R.drawable.jp ),
    new Entity( 342, "JORDAN", 0, -1, "^JY", AS, "39", "20", R.drawable.jo ),
    new Entity( 345, "BRUNEI", 0, -1, "^V8", OC, "54", "28", R.drawable.bn ),
    new Entity( 348, "KUWAIT", 0, -1, "^9K", AS, "39", "21", R.drawable.kw ),
    new Entity( 354, "LEBANON", 0, -1, "^OD", AS, "39", "20", R.drawable.lb ),
    new Entity( 363, "MONGOLIA", 0, -1, "^J[T-V]", AS, "32,33", "23", R.drawable.mn ),
    new Entity( 369, "NEPAL", 0, -1, "^9N", AS, "42", "22", R.drawable.np ),
    new Entity( 370, "OMAN", 0, -1, "^A4", AS, "39", "21", R.drawable.om ),
    new Entity( 372, "PAKISTAN", 0, -1, "^A[P-S]|^6[P-S]", AS, "41", "21", R.drawable.pk ), 
    new Entity( 375, "PHILIPPINES", 0, -1, "^D[U-Z]|^4[D-I]", OC, "50", "27", R.drawable.ph ),
    new Entity( 376, "QATAR", 0, -1, "^A7", AS, "39", "21", R.drawable.qa ),
    new Entity( 378, "SAUDI ARABIA", 0, -1, "^HZ|^8Z|^7Z", AS, "39", "21", R.drawable.sa ),
    new Entity( 379, "SEYCHELLES", 0, -1, "^S7", AF, "53", "39", R.drawable.sc ),
    new Entity( 381, "SINGAPORE", 0, -1, "^9V|^S6", AS, "54", "28", R.drawable.sg ),
    new Entity( 382, "DJIBOUTI", 0, -1, "^J2", AF, "48", "37", R.drawable.dj ),
    new Entity( 384, "SYRIA", 0, -1, "^YK|^6C", AS, "39", "20", R.drawable.sy ),
    new Entity( 386, "TAIWAN", 0, -1, "^B[M-QUWX]|^BV[^9]", AS, "44", "24", R.drawable.tw ),
    new Entity( 387, "THAILAND", 0, -1, "^E2|^HS", AS, "49", "26", R.drawable.th ),
    new Entity( 390, "TURKEY", 0, -1, "^T[A-C]|^YM", EU | AS, "17", "40", R.drawable.tr ),
    new Entity( 391, "UNITED ARAB EMIRATES", 0, -1, "^A6", AS, "39", "21", R.drawable.ae ),
    new Entity( 400, "ALGERIA", 0, -1, "^7[RT-Y]", AF, "37", "33", R.drawable.dz ),
    new Entity( 401, "ANGOLA", 0, -1, "^D2|^D3", AF, "52", "36", R.drawable.ao ),
    new Entity( 402, "BOTSWANA", 0, -1, "^A2|^8O", AF, "57", "38", R.drawable.bw ),
    new Entity( 404, "BURUNDI", 0, -1, "^9U", AF, "52", "36", R.drawable.bi ),
    new Entity( 406, "CAMEROON", 0, -1, "^TJ", AF, "47", "36", R.drawable.cm ),
    new Entity( 408, "CENTRAL AFRICAN", 0, -1, "^TL", AF, "47", "36", R.drawable.cf ),
    new Entity( 409, "CAPE VERDE", 0, -1, "^D4", AF, "46", "35", R.drawable.cv ),
    new Entity( 410, "CHAD", 0, -1, "^TT", AF, "47", "36", R.drawable.td ),
    new Entity( 411, "COMOROS", 0, -1, "^D6", ZZ, "ZZ", "ZZ", R.drawable.km ),
    new Entity( 412, "CONGO", 0, -1, "^TN", AF, "52", "36", R.drawable.cg ),
    new Entity( 414, "DEM. REP. CONGO", 0, -1, "^9[O-T]", AF, "52", "36", R.drawable.cd ),
    new Entity( 416, "BENIN", 0, -1, "^TY", AF, "46", "35", R.drawable.bj ),
    new Entity( 420, "GABON", 0, -1, "^TR", AF, "52", "36", R.drawable.ga ),
    new Entity( 422, "GAMBIA", 0, -1, "^C5", AF, "46", "35", R.drawable.gm ),
    new Entity( 424, "GHANA", 0, -1, "^9G", AF, "46", "35", R.drawable.gh ),
    new Entity( 428, "IVORY COAST", 0, -1, "^TU", AF, "46", "35", R.drawable.ci ),
    new Entity( 430, "KENYA", 0, -1, "^5Y|^5Z", AF, "48", "37", R.drawable.ke ),
    new Entity( 432, "LESOTHO", 0, -1, "^7P", AF, "57", "38", R.drawable.ls ),
    new Entity( 433, "NORTH KOREA (HP)", 0, -1, "^P[5-9]|^HM", AS, "44", "25", R.drawable.kp ),
    new Entity( 434, "LIBERIA", 0, -1, "^5[LM]|^6Z|^EL|^A8|^D5", AF, "46", "35", R.drawable.lr ),
    new Entity( 436, "LIBYA", 0, -1, "^5A", AF, "38", "34", R.drawable.ly ),
    new Entity( 438, "MADAGASCAR", 0, -1, "^5R|^5S|^6X", AF, "53", "39", R.drawable.mg ),
    new Entity( 440, "MALAWI", 0, -1, "^7Q", AF, "53", "37", R.drawable.mw ),
    new Entity( 442, "MALI", 0, -1, "^TZ", AF, "46", "35", R.drawable.ml ),
    new Entity( 444, "MAURITANIA", 0, -1, "^5T", AF, "46", "35", R.drawable.mr ),
    new Entity( 446, "MOROCCO", 0, -1, "^5[C-G]|^CN", AF, "37", "33", R.drawable.ma ),
    new Entity( 450, "NIGERIA", 0, -1, "^5N|^5O", AF, "46", "35", R.drawable.ng ),
    new Entity( 452, "ZIMBABWE", 0, -1, "^Z2", AF, "53", "38", R.drawable.zw ),
    new Entity( 453, "REUNION", 0, -1, "^FR", AF, "53", "39", R.drawable.re ),
    new Entity( 454, "RWANDA", 0, -1, "^9X", AF, "52", "36", R.drawable.rw ),
    new Entity( 456, "SENEGAL", 0, -1, "^6V|^6W", AF, "46", "35", R.drawable.sn ),
    new Entity( 458, "SIERRA LEONE", 0, -1, "^9L", AF, "46", "35", R.drawable.sl ),
    new Entity( 460, "ROTUMA ISLAND", 0, -1, "^3D2.*/R|^3D2R", OC, "56", "32", R.drawable.fj ),
    new Entity( 462, "SOUTH AFRICA", 0, -1, "^Z[R-U]|^S8", AF, "57", "38", R.drawable.za ),
    new Entity( 464, "NAMIBIA", 0, -1, "^V5", AF, "57", "38", R.drawable.na ),
    new Entity( 466, "SUDAN", 0, -1, "^ST|^6[TU]|^SS[N-Z]", AF, "48", "34", R.drawable.sd ),
    new Entity( 468, "SWAZILAND", 0, -1, "^3D[A-M6]", AF, "57", "38", R.drawable.sz ),
    new Entity( 470, "TANZANIA", 0, -1, "^5H|^5I", AF, "53", "37", R.drawable.tz ),
    new Entity( 474, "TUNISIA", 0, -1, "^3V|^TS", AF, "37", "33", R.drawable.tn ),
    new Entity( 478, "EGYPT", 0, -1, "^SU|^6[AB]|^SS[A-M]", AF, "38", "34", R.drawable.eg ),
    new Entity( 480, "BURKINA-FASO", 0, -1, "^XT", AF, "46", "35", R.drawable.bf ),
    new Entity( 482, "ZAMBIA", 0, -1, "^9[IJ]", AF, "53", "36", R.drawable.zm ),
    new Entity( 483, "TOGO", 0, -1, "^5V", AF, "46", "35", R.drawable.tg ),
    new Entity( 488, "WALVIS BAY", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 489, "CONWAY REEF", 0, -1, "^3D2C", OC, "56", "32", R.drawable.mu ),
    new Entity( 490, "BANABA ISLAND", 0, -1, "^T33", OC, "65", "31", R.drawable.ki ),
    new Entity( 492, "YEMEN", 0, -1, "^7O", AS, "39", "21", R.drawable.ye ),
    new Entity( 493, "PENGUIN ISLANDS", 1, -1, null, ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 497, "CROATIA", 0, -1, "^9A", EU, "28", "15", R.drawable.hr ),
    new Entity( 499, "SLOVENIA", 0, -1, "^S5", EU, "28", "15", R.drawable.si ),
    new Entity( 501, "BOSNIA-HERZEGOVINA", 0, -1, "^T9|^E7", EU, "28", "15", R.drawable.ba ),
    new Entity( 502, "MACEDONIA", 0, -1, "^Z3", EU, "28", "15", R.drawable.mk ),
    new Entity( 503, "CZECH REPUBLIC", 0, -1, "^OK|^OL", EU, "28", "15", R.drawable.cz ),
    new Entity( 504, "SLOVAK REPUBLIC", 0, -1, "^OM", EU, "28", "15", R.drawable.sk ),
    new Entity( 505, "PRATAS ISLAND", 0, -1, "^BV9|^BQ9", AS, "44", "24", R.drawable.tw ),
    new Entity( 506, "SCARBOROUGH REEF", 0, -1, "^BS7", AS, "50", "27", R.drawable.cn ),
    new Entity( 507, "TEMOTU ISLAND", 0, -1, "^H40", OC, "51", "32", R.drawable.sb ),
    new Entity( 508, "AUSTRAL ISLAND", 0, -1, "^FO.*/A|^TX6|^TX5", OC, "63", "32", R.drawable.pf ),
    new Entity( 509, "MARQUESAS ISLAND", 0, -1, "^FO.*/M", OC, "63", "31", R.drawable.pf ),
    new Entity( 510, "PALESTINE", 0, -1, "^E4", AS, "39", "20", R.drawable.zz ),
    new Entity( 511, "DUCIE IS", 0, 513, "^VP6.*/D", OC, "63", "32", R.drawable.pn ),
    new Entity( 512, "CHESTERFIELD IS (TX0)", 0, -1, "^TX0", ZZ, "ZZ", "ZZ", R.drawable.zz ),
    new Entity( 513, "EAST TIMOR", 0, 511, "^4W", AS, "54", "28", R.drawable.tl ),
    new Entity( 514, "MONTENEGRO", 0, -1, "^4O", EU, "28", "15", R.drawable.cs ),
    new Entity( 515, "SWAINS ISLAND", 0, -1, "^[AKNW]H8S|^[ANKW].*/KH8S$", OC, "62", "32", R.drawable.use ),    
    new Entity( 516, "SAINT BARTHELEMY", 0, -1, "^FJ", OC, "62", "32", R.drawable.bl ),
    new Entity( 517, "CURACAO", 0, -1, "^PJ2", SA, "11", "09", R.drawable.cw ),
    new Entity( 518, "SINT MAARTEN", 0, -1, "^PJ7", NA, "11", "08", R.drawable.sx),
    new Entity( 519, "SABA AND ST EUSTATIUS", 0, -1, "^PJ[5689]", NA, "11", "08", R.drawable.saba ),
    new Entity( 520, "BONAIRE", 0, -1, "^PJ4", NA, "11", "09", R.drawable.bon ),
    new Entity( 521, "SOUTH SUDAN", 0, -1, "^Z8", AF, "48", "34", R.drawable.ss ),
    new Entity( 0, "Kosovo", 0, -1, "^Z6", EU, "28", "15", R.drawable.ksv ),
}; /* gbl_dxcc */
		list = ret;
		}
		return list;
	}
}

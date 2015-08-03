package com.republic.ui.support;

import java.util.HashMap;

/**
 * Created by Akwasi Owusu on 8/3/15.
 */
public class CountryCodeList {

    //Better to have this data in Json
    private HashMap<String, String> countryCodes = new HashMap<String, String>() {{
        put("Algeria", "213");
        put("Angola", "244");
        put("Benin", "229");
        put("Botswana", "267");
        put("Burkina Faso", "226");
        put("Burundi", "257");
        put("Cameroon", "237");
        put("Cape Verde Islands", "238");
        put("Central African Republic", "236");
        put("Chad", "235");
        put("Comoros Islands", "269");
        put("Democratic Republic of the Congo", "243");
        put("Djibouti", "253");
        put("Egypt", "20");
        put("Equatorial Guinea", "240");
        put("Eritrea", "291");
        put("Ethiopia", "251");
        put("Gabon", "241");
        put("Gambia", " 220");
        put("Ghana", "233");
        put("Guinea Bissau", "245");
        put("Guinea", "224");
        put("Ivory Coast", "225");
        put("Kenya", "254");
        put("Lesotho", " 266");
        put("Liberia", "231");
        put("Libya", "218");
        put("Madagascar", "261");
        put("Malawi", "265");
        put("Mali", "223");
        put("Mauritania", "222");
        put("Mauritius", "230");
        put("Morocco", "212");
        put("Mozambique", "258");
        put("Namibia", "264");
        put("Niger", "227");
        put("Nigeria", "234");
        put("Reunion Island", "262");
        put("Rwanda", "250");
        put("Senegal", "221");
        put(" Seychelles", "248");
        put("Sierra Leone", " 232");
        put("Somalia", " 252");
        put("South Africa", " 27");
        put("Sudan", "249");
        put("Swaziland", "268");
        put("Tanzania", "255");
        put("Togo", "228");
        put("Tunisia", "216");
        put("Uganda", "256");
        put("Zambia", "260");
        put("Zimbabwe", "263");
    }};

    public HashMap<String, String> getCountryCodes() {
        return countryCodes;
    }
}

package com.republic.ui.support;

import com.republic.ui.R;

/** *
 * Created by Akwasi Owusu on 7/6/15.
 */
public class NavigationHelper {

    /**
     * Defines the main navigation items on the main page. Add to this if you
     * want to add another item on the navigation page
     *
     * @return String ids of the navigation items
     */
    public static Integer[] getMainPageNavigationItems() {
        return new Integer[]{R.string.fraud, R.string.bribery, R.string.abuse, R.string.waste,
                R.string.extortion, R.string.embezzlement, R.string.favoritism};
    }

    /**
     * Defines the main navigation item descriptions on the main page. Add to this
     * if you want to add another item on the navigation page
     *
     * @return String ids of the navigation items
     */
    public static Integer[] getMainPageDescriptions() {
        return new Integer[]{R.string.frauddescription, R.string.briberydescription, R.string.abusedescription,
                R.string.wastedescription, R.string.extortiondescription, R.string.embezzlementdescription,
                R.string.favoritismdescription};
    }

    /**
     * Returns the navigation icons used in the main page
     *
     * @return Ids for Icons used in the main page
     */
    public static Integer[] getMainPageIcons() {
        return new Integer[]{R.drawable.fraud, R.drawable.bribery, R.drawable.abuse, R.drawable.waste,
                R.drawable.extortion, R.drawable.embezzlement, R.drawable.favoritism};
    }

    public static Integer[] getNavDrawerIcons(){
        return new Integer[]{R.drawable.reportcorruption, R.drawable.republicfb, R.drawable.viewreport, R.drawable.invitefriends};
    }

    public static Integer[] getNavDrawerItems(){
        return new Integer[]{R.string.report_corruption, R.string.go_to_page, R.string.view_reports, R.string.invite_friends};
    }
}

package com.ultra.rianews2.Callbacks;

import com.ultra.rianews2.Units.NewsPreview;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(01.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public interface OnLoadNewsfeedCallback
	 {
	 void useResult(ArrayList<NewsPreview> result,String link);
	 }

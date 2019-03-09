package com.engwork.pgndb.tagssearching;

import java.util.List;

interface TagsSearchingBean {
  List<String> search(SearchTagsRequest searchTagsRequest);
}

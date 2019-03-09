package com.engwork.pgndb.tagssearching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SortWithStartValueBeanImpl implements SortWithStartValueBean {
  @Override
  public List<String> sort(List<String> data, String value) {
    List<String> first = new ArrayList<>();
    List<String> second = new ArrayList<>();
    for (String element : data) {
      if (element.toLowerCase().startsWith(value.toLowerCase())) {
        first.add(element);
      } else {
        second.add(element);
      }
    }
    Collections.sort(first);
    Collections.sort(second);
    return Stream.concat(first.stream(), second.stream()).collect(Collectors.toList());
  }
}

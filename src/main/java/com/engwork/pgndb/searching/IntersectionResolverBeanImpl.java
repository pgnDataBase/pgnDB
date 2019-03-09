package com.engwork.pgndb.searching;

import java.util.List;
import java.util.stream.Collectors;

class IntersectionResolverBeanImpl<T> implements IntersectionResolverBean<T> {
  @Override
  public List<T> resolve(List<T> list1, List<T> list2) {
    return list1.stream().filter(list2::contains).collect(Collectors.toList());
  }
}

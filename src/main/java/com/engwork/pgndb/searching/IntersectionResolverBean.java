package com.engwork.pgndb.searching;

import java.util.List;

public interface IntersectionResolverBean<T> {
  List<T> resolve(List<T> list1, List<T> list2);
}

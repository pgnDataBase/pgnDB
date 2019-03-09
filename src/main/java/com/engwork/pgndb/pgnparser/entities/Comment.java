package com.engwork.pgndb.pgnparser.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Stanisław Kabaciński.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends Entity {
  private String value;
}

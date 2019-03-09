package com.engwork.pgndb.pgnparser.pgn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Stanisław Kabaciński.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meta {

  private String key;
  private String value;
  private boolean required;
}

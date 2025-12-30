package com.sportmatch.identityservice.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sportmatch.identityservice.common.constants.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDataAPI {

  private String status;

  private Object data;

  private Object error;

  private Object meta;

  /**
   * Create successful generic response instance
   *
   * @param data Data object
   * @param meta Meta data include paging information
   * @return ResponseDataAPI
   */
  public static ResponseDataAPI success(Object data, Object meta) {
    return ResponseDataAPI.builder().status(CommonConstant.SUCCESS).data(data).meta(meta).build();
  }

  public static ResponseDataAPI successWithoutMeta(Object data) {
    return ResponseDataAPI.builder().status(CommonConstant.SUCCESS).data(data).build();
  }

  public static ResponseDataAPI successWithoutMetaAndData() {
    return ResponseDataAPI.builder().status(CommonConstant.SUCCESS).build();
  }

  /**
   * Create failed generic response instance
   *
   * @param error Error object description
   * @return ResponseDataAPI
   */
  public static ResponseDataAPI error(Object error) {
    return ResponseDataAPI.builder().status(CommonConstant.FAILURE).error(error).build();
  }
}
package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.openapitools.model.Comments;
import org.openapitools.model.Video;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * GetDataVideoById200Response
 */

@JsonTypeName("getDataVideoById_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-15T15:54:30.961535300+02:00[Europe/Madrid]", comments = "Generator version: 7.9.0")
public class GetDataVideoById200Response {

  private Video video;

  private Comments comments;

  public GetDataVideoById200Response video(Video video) {
    this.video = video;
    return this;
  }

  /**
   * Get video
   * @return video
   */
  @Valid 
  @Schema(name = "video", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("video")
  public Video getVideo() {
    return video;
  }

  public void setVideo(Video video) {
    this.video = video;
  }

  public GetDataVideoById200Response comments(Comments comments) {
    this.comments = comments;
    return this;
  }

  /**
   * Get comments
   * @return comments
   */
  @Valid 
  @Schema(name = "comments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comments")
  public Comments getComments() {
    return comments;
  }

  public void setComments(Comments comments) {
    this.comments = comments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetDataVideoById200Response getDataVideoById200Response = (GetDataVideoById200Response) o;
    return Objects.equals(this.video, getDataVideoById200Response.video) &&
        Objects.equals(this.comments, getDataVideoById200Response.comments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(video, comments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetDataVideoById200Response {\n");
    sb.append("    video: ").append(toIndentedString(video)).append("\n");
    sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


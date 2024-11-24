package org.openapitools.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import javax.annotation.Generated;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Comments
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-15T15:54:30.961535300+02:00[Europe/Madrid]", comments = "Generator version: 7.9.0")
public class Comments {

  private Long id;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime uploadDate;

  private Long user;

  private Long video;

  private String text;
  
  private String username;

  public Comments id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * ID único del video (clave primaria).
   * @return id
   */
  
  @Schema(name = "id", description = "ID único del video (clave primaria).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Comments uploadDate(OffsetDateTime uploadDate) {
    this.uploadDate = uploadDate;
    return this;
  }

  /**
   * Fecha y hora de subida del comentario.
   * @return uploadDate
   */
  @Valid 
  @Schema(name = "uploadDate", description = "Fecha y hora de subida del comentario.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uploadDate")
  public OffsetDateTime getUploadDate() {
    return uploadDate;
  }

  public void setUploadDate(OffsetDateTime uploadDate) {
    this.uploadDate = uploadDate;
  }

  public Comments user(Long user) {
    this.user = user;
    return this;
  }

  /**
   * Clave que referencia al usuario que ha realizado el comentario.
   * @return user
   */
  
  @Schema(name = "user", description = "Clave que referencia al usuario que ha realizado el comentario.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user")
  public Long getUser() {
    return user;
  }

  public void setUser(Long user) {
    this.user = user;
  }

  public Comments video(Long video) {
    this.video = video;
    return this;
  }

  /**
   * Clave que referencia al video en el que ha realizado el comentario.
   * @return video
   */
  
  @Schema(name = "video", description = "Clave que referencia al video en el que ha realizado el comentario.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("video")
  public Long getVideo() {
    return video;
  }

  public void setVideo(Long video) {
    this.video = video;
  }

  public Comments text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Texto del comentario.
   * @return text
   */
  
  @Schema(name = "text", description = "Texto del comentario.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Schema(name = "username", description = "Nombre de usuario del comentario", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
	    this.username = username;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comments comments = (Comments) o;
    return Objects.equals(this.id, comments.id) &&
        Objects.equals(this.uploadDate, comments.uploadDate) &&
        Objects.equals(this.user, comments.user) &&
        Objects.equals(this.video, comments.video) &&
        Objects.equals(this.text, comments.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, uploadDate, user, video, text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comments {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    uploadDate: ").append(toIndentedString(uploadDate)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    video: ").append(toIndentedString(video)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
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


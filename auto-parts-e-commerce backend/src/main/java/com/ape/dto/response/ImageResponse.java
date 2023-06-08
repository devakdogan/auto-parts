package com.ape.dto.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ImageResponse extends Response{
    private Set<String> imageId;

   public ImageResponse(Set<String> imageId, String message, boolean success) {
       super(message, success);
    this.imageId=imageId;

   }


}

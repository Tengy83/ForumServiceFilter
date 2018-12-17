package telran.ashkelon2018.forum.dto;

import java.util.Set;

import lombok.Getter;

@Getter
public class PostUpdateDto {
	private String id;
	private String title;
	private String content;
	private Set<String> tags;
}

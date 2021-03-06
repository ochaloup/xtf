package cz.xtf.openshift.imagestream;

import org.junit.Test;

import static cz.xtf.openshift.imagestream.ImageStreamRequest.builder;
import static org.assertj.core.api.Assertions.assertThat;

public class ImageStreamRequestTest {

	private static final String IMAGE = "image";
	private static final String NAME = "name";

	@Test(expected = NullPointerException.class)
	public void nameShouldBeRequired() {
		builder().imageName(IMAGE).build();
	}

	@Test(expected = NullPointerException.class)
	public void imageShouldBeRequired() {
		builder().name(NAME).build();
	}

	@Test
	public void tagsShouldBeOptional() {
		builder().name(NAME).imageName(IMAGE).build();
	}

	@Test
	public void testBuilder() {
		final String[] tags = {"tag#1", "tag#2"};
		final ImageStreamRequest isr = builder().name(NAME).imageName(IMAGE).tag(tags[0]).tag(tags[1]).build();
		assertThat(isr.getName()).isEqualTo(NAME);
		assertThat(isr.getImageName()).isEqualTo(IMAGE);
		assertThat(isr.getTags()).containsExactly(tags);
	}

	@Test
	public void noTagsShouldBeRepresentedAsEmptyCollection() {
		assertThat(builder().name(NAME).imageName(IMAGE).build().getTags()).isNotNull().isEmpty();
	}

	@Test
	public void imageShouldByRetrievedByNameFromImageRegistry() {
		assertThat(builder().name(NAME).imageName("eap7").build().getImage()).isEqualTo(ImageRegistry.get().eap7());
	}

	@Test(expected = IllegalStateException.class)
	public void illegalStateExpectedWhenImageIsNotFoundInImageRegistry() {
		builder().name(NAME).imageName("xyz").build().getImage();
	}
}

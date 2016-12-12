package ch.uzh.ifi.climateapp.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.text.client.IntegerParser;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.ValueBox;

public class YearBox extends ValueBox<Integer>{
	public YearBox() {
		super(Document.get().createTextInputElement(),
				new AbstractRenderer<Integer>() {
					public String render(Integer i) {
						return i==null?"":i.toString();
					}
				},
				IntegerParser.instance());
	}
}

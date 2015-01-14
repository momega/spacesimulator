package com.momega.spacesimulator.swing;

import com.momega.spacesimulator.renderer.RendererModel;

public class IndexModel extends AbstractObjectsModel<Integer> {

	private static final long serialVersionUID = -2784330874079672872L;

	public IndexModel() {
		super(RendererModel.getInstance().getAvailableIndexes());
	}

}

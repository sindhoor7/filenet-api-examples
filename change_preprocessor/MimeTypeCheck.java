package com.ibm.idea.changepreprocessor;

import java.util.HashSet;
import java.util.Set;

import com.filenet.api.action.Checkin;
import com.filenet.api.action.Create;
import com.filenet.api.action.PendingAction;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.engine.ChangePreprocessor;
import com.filenet.api.exception.EngineRuntimeException;

public class MimeTypeCheck implements ChangePreprocessor {

	@Override
	public boolean preprocessObjectChange(
			IndependentlyPersistableObject sourceObj)
			throws EngineRuntimeException {

		try {
			boolean checkMimeType = false;
			String allowedMimeTypes[] = { "image/tif", "image/tiff",
					"application/pdf", "image/bmp", "image/jpeg", "image/jpg" };

			Set<String> blockedSet = new HashSet<String>();
			for (int i = 0; i < allowedMimeTypes.length; i++) {
				blockedSet.add(allowedMimeTypes[i]);
			}
			// Check MimeType property only for create and checkin actions.
			PendingAction actions[] = sourceObj.getPendingActions();

			for (int i = 0; i < actions.length && !checkMimeType; i++) {
				if (actions[i] instanceof Create
						|| actions[i] instanceof Checkin) {
					checkMimeType = true;
				}
			}
			if (!checkMimeType) {
				return false;
			}
			// Verify that MimeType property is in collection before attempting
			// to retrieve it.
			if (sourceObj.getProperties().isPropertyPresent(
					PropertyNames.MIME_TYPE)) {
				// Change MimeType value if it does not conform to the
				// requirement.
				String mimeType = sourceObj.getProperties()
						.get(PropertyNames.MIME_TYPE).getStringValue();
				if (!blockedSet.contains(mimeType)) {
					throw new RuntimeException(
							"The action is prohibited as the source object is of type "
									+ mimeType);
				}
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

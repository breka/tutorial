package org.imogene.model.validation.constraints;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.imogene.model.core.CardEntity;
import org.imogene.model.core.FieldEntity;


public class CheckEntityMainFields extends AbstractMedanyModelConstraint {
	
	private static String NOT_IN_COLUMNS = "The main field \"%FIELD_NAME%\" of the entity \"%ENTITY_NAME%\" doesn't belong to the the column fields";
	
	@Override
	public IStatus validate(IValidationContext ctx) {
		try {
			if (ctx.getTarget() instanceof CardEntity) {
				CardEntity entity = (CardEntity) ctx.getTarget();
				for (FieldEntity field : entity.getMainFields())
					if (!entity.getColumnFields().contains(field))
						return ctx.createFailureStatus(new Object[] {formatMessage(NOT_IN_COLUMNS, entity, field)});
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ctx.createSuccessStatus();
		}
		return ctx.createSuccessStatus();
	}

}

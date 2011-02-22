package org.imogene.android.oaw.generator;

import java.util.ArrayList;
import java.util.List;

import org.imogene.model.core.CardEntity;
import org.imogene.model.core.Description;
import org.imogene.model.core.FieldDependentVisibility;
import org.imogene.model.core.FieldEntity;
import org.imogene.model.core.FieldGroup;
import org.imogene.model.core.GeoField;
import org.imogene.model.core.Project;
import org.imogene.model.core.RelationFieldEntity;
import org.imogene.model.core.ReverseRelationFieldEntity;
import org.imogene.model.core.Thema;


/**
 */
public class MedooAndroidGenHelper {
	
	public static final void log(Object o) {
		System.out.println(o);
	}

	public static final int getDatabaseVersion() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static boolean hasDependancyField(FieldEntity field) {
		for (FieldGroup group : field.getParentCard().getGroups()) {
			for (FieldEntity f : group.getFields()) {
				for (FieldDependentVisibility fdv : f
						.getFieldDependentVisibility()) {
					if (fdv.getDependencyField().getShortName().equals(
							field.getShortName()))
						return true;
				}
			}
		}
		return false;
	}

	public static boolean hasHelp(List<Description> list) {
		for (Description des : list) {
			String help = des.getHelp();
			if (help != null && !help.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static List<RelationFieldEntity> getMoreFields(
			List<FieldEntity> fields) {
		ArrayList<RelationFieldEntity> result = new ArrayList<RelationFieldEntity>();
		for (FieldEntity field : fields) {
			if (!field.isHidden()) {
				if (field instanceof RelationFieldEntity)
					if (((RelationFieldEntity) field)
							.getOppositeRelationField() != null
							&& ((RelationFieldEntity) field).getCardinality() != 1) {
						result.add((RelationFieldEntity) field);
					}
			}
		}
		return result;
	}

	public static boolean hasMiscellaneousThema(List<CardEntity> entities,
			List<Thema> themas) {
		for (CardEntity entity : entities) {
			if (isInMiscellaneousThema(themas, entity))
				return true;
		}
		return false;
	}

	public static boolean isInMiscellaneousThema(List<Thema> themas,
			CardEntity entity) {
		if (!entity.isTopLevel())
			return false;
		for (Thema thema : themas) {
			if (thema.getEntities().contains(entity))
				return false;
		}
		return true;
	}

	public static boolean hasQRCode(Project project) {
		for (CardEntity entity : project.getEntities()) {
			if (entity.isDisplayQRCode())
				return true;
		}
		return false;
	}
	
	public static List<FieldEntity> getMainFields(CardEntity entity) {
		ArrayList<FieldEntity> main = new ArrayList<FieldEntity>();
		for (FieldEntity field : entity.getMainFields())
			if (!entity.getSecondaryFields().contains(field))
				main.add(field);
		return main;
	}
	
	public static boolean isForbiddenCase(FieldEntity field) {
		if (field instanceof ReverseRelationFieldEntity) {
			ReverseRelationFieldEntity rel = (ReverseRelationFieldEntity) field;
			return rel.getCardinality() == 1 && rel.getOppositeRelationField() != null && rel.getOppositeRelationField().getCardinality() == 1;
		}
		return false;
	}
	
	public static List<RelationFieldEntity> getFilteredFields(CardEntity entity, FieldEntity filter) {
		ArrayList<RelationFieldEntity> result = new ArrayList<RelationFieldEntity>();
		if (filter instanceof RelationFieldEntity)
			for (FieldGroup group : entity.getGroups())
				for (FieldEntity field: group.getFields())
					if (field instanceof RelationFieldEntity) {
						RelationFieldEntity relation = (RelationFieldEntity) field;
						if (relation.getRelationHierarchicalFilter() != null && relation.getRelationHierarchicalFilter().size() == 2)
							if (relation.getRelationHierarchicalFilter().get(0).equals(filter))
								result.add(relation);
					}
		return result;
	}
	
	public static Integer mod2(Integer i) {
		return i % 2;
	}
	
	public static boolean hasFilter(Project project) {
		for (CardEntity entity : project.getEntities())
			if (entity.isClientPeriodFilterable() || entity.getClientFilterFields().size() > 0)
				return true;
		return false;
	}
	
	public static List<FieldGroup> getFilteredGroups(List<FieldEntity> filters) {
		ArrayList<FieldGroup> result = new ArrayList<FieldGroup>();
		for (FieldEntity field : filters)
			if (!result.contains(field.getParentGroup()))
				result.add(field.getParentGroup());
		return result;
	}
	
	public static GeoField findGis(CardEntity entity) {
		for (FieldGroup group : entity.getGroups())
			for (FieldEntity field : group.getFields())
				if (field instanceof GeoField)
					return (GeoField) field;
		return null;
	}
	
	public static int getGeoType(GeoField field) {
		return field.getType().getValue();
	}

}

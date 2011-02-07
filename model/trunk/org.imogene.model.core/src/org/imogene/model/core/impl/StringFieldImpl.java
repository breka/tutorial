/**
 * Medes-IMPS 2011
 *
 * $Id$
 */
package org.imogene.model.core.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.imogene.model.core.ImogenePackage;
import org.imogene.model.core.StringField;
import org.imogene.model.core.ValidationRule;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>String Field</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.imogene.model.core.impl.StringFieldImpl#getValidationRules <em>Validation Rules</em>}</li>
 *   <li>{@link org.imogene.model.core.impl.StringFieldImpl#isTranslatable <em>Translatable</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StringFieldImpl extends FieldEntityImpl implements StringField {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Medes-IMPS 2011";

	/**
	 * The cached value of the '{@link #getValidationRules() <em>Validation Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValidationRules()
	 * @generated
	 * @ordered
	 */
	protected EList<ValidationRule> validationRules;

	/**
	 * The default value of the '{@link #isTranslatable() <em>Translatable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTranslatable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TRANSLATABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTranslatable() <em>Translatable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTranslatable()
	 * @generated
	 * @ordered
	 */
	protected boolean translatable = TRANSLATABLE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StringFieldImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ImogenePackage.Literals.STRING_FIELD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ValidationRule> getValidationRules() {
		if (validationRules == null) {
			validationRules = new EObjectContainmentEList<ValidationRule>(ValidationRule.class, this, ImogenePackage.STRING_FIELD__VALIDATION_RULES);
		}
		return validationRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTranslatable() {
		return translatable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTranslatable(boolean newTranslatable) {
		boolean oldTranslatable = translatable;
		translatable = newTranslatable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ImogenePackage.STRING_FIELD__TRANSLATABLE, oldTranslatable, translatable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ImogenePackage.STRING_FIELD__VALIDATION_RULES:
				return ((InternalEList<?>)getValidationRules()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ImogenePackage.STRING_FIELD__VALIDATION_RULES:
				return getValidationRules();
			case ImogenePackage.STRING_FIELD__TRANSLATABLE:
				return isTranslatable();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ImogenePackage.STRING_FIELD__VALIDATION_RULES:
				getValidationRules().clear();
				getValidationRules().addAll((Collection<? extends ValidationRule>)newValue);
				return;
			case ImogenePackage.STRING_FIELD__TRANSLATABLE:
				setTranslatable((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ImogenePackage.STRING_FIELD__VALIDATION_RULES:
				getValidationRules().clear();
				return;
			case ImogenePackage.STRING_FIELD__TRANSLATABLE:
				setTranslatable(TRANSLATABLE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ImogenePackage.STRING_FIELD__VALIDATION_RULES:
				return validationRules != null && !validationRules.isEmpty();
			case ImogenePackage.STRING_FIELD__TRANSLATABLE:
				return translatable != TRANSLATABLE_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (translatable: ");
		result.append(translatable);
		result.append(')');
		return result.toString();
	}

} //StringFieldImpl

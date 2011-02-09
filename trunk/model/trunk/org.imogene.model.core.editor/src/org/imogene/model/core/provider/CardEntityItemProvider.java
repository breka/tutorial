/**
 * Medes-IMPS 2011
 *
 * $Id$
 */
package org.imogene.model.core.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.imogene.model.core.CardEntity;
import org.imogene.model.core.ImogeneFactory;
import org.imogene.model.core.ImogenePackage;
import org.imogene.model.core.editor.ImogeneModelEditPlugin;

/**
 * This is the item provider adapter for a {@link org.imogene.model.core.CardEntity} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CardEntityItemProvider
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Medes-IMPS 2011";

	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CardEntityItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addNamePropertyDescriptor(object);
			addShortNamePropertyDescriptor(object);
			addColumnFieldsPropertyDescriptor(object);
			addMainFieldsPropertyDescriptor(object);
			addSecondaryFieldsPropertyDescriptor(object);
			addIconPropertyDescriptor(object);
			addTopLevelPropertyDescriptor(object);
			addClientFilterFieldsPropertyDescriptor(object);
			addSortFieldsPropertyDescriptor(object);
			addActorFilterFieldsPropertyDescriptor(object);
			addColorPropertyDescriptor(object);
			addDisplayQRCodePropertyDescriptor(object);
			addCreatorsPropertyDescriptor(object);
			addClientPeriodFilterablePropertyDescriptor(object);
			addDirectAccessPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_name_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Main Fields feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMainFieldsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_mainFields_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_mainFields_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__MAIN_FIELDS,
				 true,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Column Fields feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addColumnFieldsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_columnFields_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_columnFields_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__COLUMN_FIELDS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Short Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShortNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_shortName_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_shortName_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__SHORT_NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Icon feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIconPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_icon_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_icon_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__ICON,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Top Level feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTopLevelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_topLevel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_topLevel_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__TOP_LEVEL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Client Filter Fields feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addClientFilterFieldsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_clientFilterFields_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_clientFilterFields_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__CLIENT_FILTER_FIELDS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sort Fields feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSortFieldsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_sortFields_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_sortFields_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__SORT_FIELDS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Actor Filter Fields feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addActorFilterFieldsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_actorFilterFields_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_actorFilterFields_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__ACTOR_FILTER_FIELDS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Secondary Fields feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSecondaryFieldsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_secondaryFields_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_secondaryFields_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__SECONDARY_FIELDS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Color feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addColorPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_color_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_color_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__COLOR,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Display QR Code feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDisplayQRCodePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_DisplayQRCode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_DisplayQRCode_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__DISPLAY_QR_CODE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Creators feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCreatorsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_creators_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_creators_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__CREATORS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Client Period Filterable feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addClientPeriodFilterablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_clientPeriodFilterable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_clientPeriodFilterable_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__CLIENT_PERIOD_FILTERABLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Direct Access feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDirectAccessPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CardEntity_directAccess_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CardEntity_directAccess_feature", "_UI_CardEntity_type"),
				 ImogenePackage.Literals.CARD_ENTITY__DIRECT_ACCESS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(ImogenePackage.Literals.CARD_ENTITY__DESCRIPTIONS);
			childrenFeatures.add(ImogenePackage.Literals.CARD_ENTITY__GROUPS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns CardEntity.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CardEntity"));
	}

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * @generated NOT
     */
    public String getText(Object object)
    {
        CardEntity c = (CardEntity) object;
        String label = c.getName();
        return label == null || label.length() == 0 ?
            getString("_UI_CardEntity_type") :
            getString("_UI_CardEntity_type") + " " + label + "(" + c.getShortName() + ")";
    }

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(CardEntity.class)) {
			case ImogenePackage.CARD_ENTITY__NAME:
			case ImogenePackage.CARD_ENTITY__SHORT_NAME:
			case ImogenePackage.CARD_ENTITY__ICON:
			case ImogenePackage.CARD_ENTITY__TOP_LEVEL:
			case ImogenePackage.CARD_ENTITY__COLOR:
			case ImogenePackage.CARD_ENTITY__DISPLAY_QR_CODE:
			case ImogenePackage.CARD_ENTITY__CLIENT_PERIOD_FILTERABLE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ImogenePackage.CARD_ENTITY__DESCRIPTIONS:
			case ImogenePackage.CARD_ENTITY__GROUPS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(ImogenePackage.Literals.CARD_ENTITY__DESCRIPTIONS,
				 ImogeneFactory.eINSTANCE.createDescription()));

		newChildDescriptors.add
			(createChildParameter
				(ImogenePackage.Literals.CARD_ENTITY__GROUPS,
				 ImogeneFactory.eINSTANCE.createFieldGroup()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ImogeneModelEditPlugin.INSTANCE;
	}

}

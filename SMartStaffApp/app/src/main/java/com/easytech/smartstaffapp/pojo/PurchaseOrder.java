package com.easytech.smartstaffapp.pojo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bana on 9/4/16.
 */
public class PurchaseOrder implements Serializable {

    private Long Id;
    private String POTitle;

    private String PODescription;
    private Set<Item> stock = new HashSet<>();
    private Employee requestor = new Employee();
    private String supplierName;
    private boolean PODeclareCOI;
    private Employee approver = new Employee();
    private boolean POApprovalStatus;
    private long POApprovalDate;
    private long POdeliveryDate;
    private boolean deleteStatus;
    private boolean scheduledPO;
    private boolean scheduleRunCompleted;
    private String POScheduleRepeatDays;
    private long PONextRunDate;
    private boolean draftStatus;
    private boolean deliveryCompleted;
    private boolean completedInvoice;
}

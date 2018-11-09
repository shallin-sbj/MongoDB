package com.mongodb.transaction.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LayoutFieMessage implements Serializable {

    private List<LayoutFile> layoutFiles;

}

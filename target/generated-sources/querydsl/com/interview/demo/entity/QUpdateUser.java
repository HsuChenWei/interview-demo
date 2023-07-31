package com.interview.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUpdateUser is a Querydsl query type for UpdateUser
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUpdateUser extends EntityPathBase<UpdateUser> {

    private static final long serialVersionUID = 1222297038L;

    public static final QUpdateUser updateUser = new QUpdateUser("updateUser");

    public final StringPath id = createString("id");

    public final StringPath userName = createString("userName");

    public final StringPath userPwd = createString("userPwd");

    public QUpdateUser(String variable) {
        super(UpdateUser.class, forVariable(variable));
    }

    public QUpdateUser(Path<? extends UpdateUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUpdateUser(PathMetadata metadata) {
        super(UpdateUser.class, metadata);
    }

}


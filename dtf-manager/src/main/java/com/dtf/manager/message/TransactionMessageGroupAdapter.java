package com.dtf.manager.message;

/**
 * Adapter of transaction message group.
 * 
 * @author wangguangyuan
 */
public abstract class TransactionMessageGroupAdapter implements TransactionMessageGroupInterface<String> {
    
    /**
     * Override method.
     */
    @Override
    public void addMember(final String memberId, final String url, final Object obj, final String httpAction) {
    }
    
    /**
     * Override method.
     *
     * @return null
     */
    @Override
    public String getGroupMemberId() {
        return null;
    }
    
    /**
     * Override method.
     */
    @Override
    public void setCommitted(final boolean commited) {
    }
    
    /**
     * Override method.
     */
    @Override
    public void setGroupId(final String groupId) {
    }
    
    /**
     * Override method.
     *
     * @return null
     */
    @Override
    public Boolean isCommitted() {
        return null;
    }
    
    /**
     * Override method.
     *
     * @return null
     */
    @Override
    public String getUrl() {
        return null;
    }
    
    /**
     * Override method.
     *
     * @return null
     */
    @Override
    public Object getObj() {
        return null;
    }
    
}

package com.remotehcs.remotehcs.record;


public class PatientSearch
{
    private PatientData[] results;

    private String previous;

    private Integer count;

    private String next;

    public PatientData[] getResults ()
    {
        return results;
    }

    public PatientData getResults (int i)
    {
        return results[i];
    }

    public void setResults (PatientData[] results)
    {
        this.results = results;
    }

    public String getPrevious ()
{
    return previous;
}

    public void setPrevious (String previous)
    {
        this.previous = previous;
    }

    public Integer getCount ()
    {
        return count;
    }

    public void setCount (Integer count)
    {
        this.count = count;
    }

    public String getNext ()
{
    return next;
}

    public void setNext (String next)
    {
        this.next = next;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [results = "+results+", previous = "+previous+", count = "+count+", next = "+next+"]";
    }
}

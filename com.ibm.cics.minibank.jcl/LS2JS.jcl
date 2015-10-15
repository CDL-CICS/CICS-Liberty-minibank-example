//L2JB02   JOB (MYSYS,AUSER),MSGCLASS=H,                             
//         CLASS=A,NOTIFY=&SYSUID,REGION=0M                          
//LS2JS JCLLIB ORDER=(ANTZ.CICS.TS.DEV.INTEGRAT.SDFHINST)            
//WSBIND EXEC DFHLS2JS,                                              
//    JAVADIR='/java/java71_bit64_sr1/J7.1_64',                      
//    USSDIR='devint',                                               
//    PATHPREF='',                                                   
//    TMPDIR='/tmp',                                                 
//    TMPFILE='l'                                                    
//INPUT.SYSUT1 DD *                                                  
LOGFILE=/u/amilybj/zosConnect/ls2js/log/ls2js.log                    
PDSLIB=//AMILYBJ.CICS.MINIBANK                                       
REQMEM=TRANSFER                                                      
RESPMEM=TRANSFER                                                     
LANG=COBOL                                                           
WSBIND=/u/amilybj/zosConnect/ls2js/wsbind/trasferJSON.wsbind         
JSON-SCHEMA-REQUEST=/u/amilybj/zosConnect/ls2js/schema/transfQ.json  
JSON-SCHEMA-RESPONSE=/u/amilybj/zosConnect/ls2js/schema/transfP.json 
MAPPING-LEVEL=3.0                                                    
PGMNAME=TRANSFER                                                     
PGMINT=COMMAREA                                                      
URI=/minibankAPI/transfer                                            
DATETIME=PACKED15                                                    
SYNCONRETURN=YES                                                     
CCSID=935                                                            
/*                                                                   
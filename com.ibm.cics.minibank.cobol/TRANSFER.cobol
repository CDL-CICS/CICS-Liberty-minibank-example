       IDENTIFICATION DIVISION.
       PROGRAM-ID. TRANSFER.
       AUTHOR.     ZHOU DONG SHENG/CHINA/IBM.
       DATE-WRITTEN. 2013-03-12.
       ENVIRONMENT DIVISION.
       DATA DIVISION.
       WORKING-STORAGE SECTION.
           EXEC SQL INCLUDE SQLDA END-EXEC.
           EXEC SQL INCLUDE SQLCA END-EXEC.
           EXEC SQL INCLUDE ACCOUNT END-EXEC.
           EXEC SQL INCLUDE TRANHIST END-EXEC.

       01 WS-COMMAREA.
           COPY TRANSFER.
       01 WS-ACCOUNT         PIC X(10).
       01 WS-TRANS-AMOUNT    PIC S9(12)V9(3) VALUE 0.
       01 AM-TEMP            PIC S9(12)V9(3) VALUE 0.

       LINKAGE SECTION.
       01 DFHCOMMAREA.
           COPY TRANSFER.

       PROCEDURE DIVISION.
       0000-MAIN.
           MOVE DFHCOMMAREA  TO WS-COMMAREA.
           MOVE '0'          TO TRAN-RESULT  OF WS-COMMAREA.
           EXEC SQL SET CURRENT SCHEMA='CTUSERS' END-EXEC.
           PERFORM A000-CHECK-INPUT          THRU A000-EXIT.
           PERFORM A100-UPD-SOURCE-ACCOUNT   THRU A100-EXIT.
           PERFORM A200-UPD-TARGET-ACCOUNT   THRU A200-EXIT.
           PERFORM A300-UPD-TRANS-LOG-SOURCE THRU A300-EXIT.
           PERFORM A400-UPD-TRANS-LOG-TARGET THRU A400-EXIT.
           PERFORM A500-RETURN               THRU A500-EXIT.
       0000-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A000-CHECK-INPUT.
           IF SOURCE-ACCOUNT OF WS-COMMAREA =
              TARGET-ACCOUNT OF WS-COMMAREA
              MOVE '1' TO TRAN-RESULT OF WS-COMMAREA
              MOVE LOW-VALUE TO ERROR-MSG OF WS-COMMAREA
              MOVE 'TWO ACCOUNTS CAN NOT BE SAME' TO
                                        ERROR-MSG OF WS-COMMAREA
              PERFORM E000-ERROR THRU E000-EXIT
           END-IF.
       A000-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A100-UPD-SOURCE-ACCOUNT.
           MOVE SOURCE-ACCOUNT OF WS-COMMAREA
                            TO WS-ACCOUNT.
           MOVE TRANS-AMOUNT OF WS-COMMAREA TO WS-TRANS-AMOUNT.
           COMPUTE WS-TRANS-AMOUNT = WS-TRANS-AMOUNT * -1.
           PERFORM A888-CHECK-BALANCE THRU A888-EXIT.
           PERFORM A777-UPD-ACCOUNT THRU A777-EXIT.
       A100-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A200-UPD-TARGET-ACCOUNT.
           MOVE TARGET-ACCOUNT OF WS-COMMAREA TO WS-ACCOUNT.
           MOVE TRANS-AMOUNT OF WS-COMMAREA TO WS-TRANS-AMOUNT.
           PERFORM A777-UPD-ACCOUNT THRU A777-EXIT.
       A200-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A300-UPD-TRANS-LOG-SOURCE.
           MOVE SOURCE-ACCOUNT OF WS-COMMAREA TO WS-ACCOUNT.
           MOVE TRANS-AMOUNT OF WS-COMMAREA TO WS-TRANS-AMOUNT.
           COMPUTE WS-TRANS-AMOUNT = WS-TRANS-AMOUNT * -1.
           PERFORM A999-UPD-TRANS-LOG THRU A999-EXIT.
       A300-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A400-UPD-TRANS-LOG-TARGET.
           MOVE TARGET-ACCOUNT OF WS-COMMAREA TO WS-ACCOUNT.
           MOVE TRANS-AMOUNT OF WS-COMMAREA TO WS-TRANS-AMOUNT.
           PERFORM A999-UPD-TRANS-LOG THRU A999-EXIT.
       A400-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A500-RETURN.
           MOVE '0' TO TRAN-RESULT OF WS-COMMAREA
           MOVE LOW-VALUE TO ERROR-MSG OF WS-COMMAREA
           MOVE 'TRANSFER SUCCESSFULLY' TO ERROR-MSG OF WS-COMMAREA
           PERFORM RETURN-CALL THRU RETURN-EXIT.
       A500-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A777-UPD-ACCOUNT.
           MOVE WS-ACCOUNT TO ACCOUNTNUMBER OF DCLACCOUNT
           MOVE WS-TRANS-AMOUNT TO BALANCE  OF DCLACCOUNT
           EXEC SQL
                UPDATE ACCOUNT
                       SET (BALANCE
                           ,LASTCHANGETIME
                           )=
                           (BALANCE + :DCLACCOUNT.BALANCE
                           ,CURRENT TIMESTAMP
                           )
                     WHERE ACCOUNTNUMBER =:DCLACCOUNT.ACCOUNTNUMBER
           END-EXEC.
           EVALUATE SQLCODE
             WHEN 0
                     CONTINUE
             WHEN 100
                     MOVE '1' TO TRAN-RESULT OF WS-COMMAREA
                     MOVE LOW-VALUE TO ERROR-MSG OF WS-COMMAREA
                     MOVE 'THE ACCOUNT IS NOT FOUND' TO
                                       ERROR-MSG OF WS-COMMAREA
                     PERFORM E000-ERROR THRU E000-EXIT
             WHEN OTHER
                     MOVE '1' TO TRAN-RESULT OF WS-COMMAREA
                     MOVE LOW-VALUE TO ERROR-MSG OF WS-COMMAREA
                     MOVE 'INTERNAL ERROR' TO ERROR-MSG OF WS-COMMAREA
                     PERFORM E000-ERROR THRU E000-EXIT
           END-EVALUATE.
       A777-EXIT.
           EXIT.
      *--------------------------------------------------------------*
       A888-CHECK-BALANCE.
           MOVE WS-ACCOUNT TO ACCOUNTNUMBER OF DCLACCOUNT
           EXEC SQL
               SELECT BALANCE
                 INTO :DCLACCOUNT.BALANCE
                 FROM ACCOUNT
                WHERE ACCOUNTNUMBER =:DCLACCOUNT.ACCOUNTNUMBER
                 WITH RS USE AND KEEP UPDATE LOCKS
           END-EXEC
           IF SQLCODE < 0
              MOVE '1' TO TRAN-RESULT OF WS-COMMAREA
              MOVE LOW-VALUE TO ERROR-MSG OF WS-COMMAREA
              MOVE 'INTERNAL ERROR' TO ERROR-MSG OF WS-COMMAREA
              PERFORM E000-ERROR THRU E000-EXIT
           END-IF.
           COMPUTE AM-TEMP = BALANCE     OF DCLACCOUNT
                           + WS-TRANS-AMOUNT.
           IF AM-TEMP < 0
              MOVE '1' TO TRAN-RESULT OF WS-COMMAREA
              MOVE LOW-VALUE TO ERROR-MSG OF WS-COMMAREA
              MOVE 'INSUFFIENT AMOUNT' TO ERROR-MSG OF WS-COMMAREA
              PERFORM E000-ERROR THRU E000-EXIT
           END-IF.
       A888-EXIT.
             EXIT.
      *--------------------------------------------------------------*
       A999-UPD-TRANS-LOG.
           MOVE 'TRANSFER'    TO TRANSNAME  OF DCLTRANSHISTORY
           MOVE WS-ACCOUNT TO ACCOUNTNUMBER OF DCLTRANSHISTORY
           MOVE WS-TRANS-AMOUNT TO TRANSAMOUNT  OF DCLTRANSHISTORY
           EXEC SQL
                INSERT INTO TRANSHISTORY
                           (TRANSNAME
                           ,ACCOUNTNUMBER
                           ,TRANSAMOUNT
                           ,TRANSTIME
                           )
                 VALUES
                           (:DCLTRANSHISTORY.TRANSNAME
                           ,:DCLTRANSHISTORY.ACCOUNTNUMBER
                           ,:DCLTRANSHISTORY.TRANSAMOUNT
                           ,CURRENT TIMESTAMP
                           )
           END-EXEC.
           EVALUATE SQLCODE
             WHEN 0
                     CONTINUE
             WHEN OTHER
                     MOVE '1' TO TRAN-RESULT OF WS-COMMAREA
                     MOVE LOW-VALUE TO ERROR-MSG OF WS-COMMAREA
                     MOVE 'INTERNAL ERROR' TO ERROR-MSG OF WS-COMMAREA
                     PERFORM E000-ERROR       THRU E000-EXIT
           END-EVALUATE.
       A999-EXIT.
           EXIT.
      *--------------------------------------------------------------*
       E000-ERROR.
           IF TRAN-RESULT OF WS-COMMAREA = '1'
              EXEC CICS SYNCPOINT ROLLBACK END-EXEC
           END-IF.
           PERFORM RETURN-CALL THRU RETURN-EXIT.
       E000-EXIT.
           EXIT.
      *--------------------------------------------------------------*
       RETURN-CALL.
           MOVE WS-COMMAREA TO DFHCOMMAREA.
           EXEC CICS
                RETURN
           END-EXEC.
       RETURN-EXIT.
           EXIT.
      *--------------------------------------------------------------*

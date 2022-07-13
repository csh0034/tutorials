package com.ask.springjpacore.entity.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_notice")
@Getter
@Setter
@ToString
public class Notice {

  @Id
//  @GenericGenerator(
//      name = "idGenerator",
//      strategy = "org.hibernate.id.enhanced.TableGenerator",
//      parameters = {
//          @Parameter(name = TableGenerator.TABLE_PARAM, value = "tb_ids"),
//          @Parameter(name = TableGenerator.SEGMENT_COLUMN_PARAM, value = "sequence_name"),
//          @Parameter(name = TableGenerator.SEGMENT_VALUE_PARAM, value = "mt_notice"),
//          @Parameter(name = TableGenerator.SEGMENT_LENGTH_PARAM, value = "200")
//      }
//  )
  @TableGenerator(
      name = "idGenerator",
      table = "tb_ids",
      pkColumnName = "sequence_name",
      valueColumnName = "ext_val",
      pkColumnValue = "mt_notice",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "idGenerator")
  private Long id;

}

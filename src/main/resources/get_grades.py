# Copyright (C) 2022 by the XiDian Open Source Community.
#
# This file is part of xidian-scripts.
#
# xidian-scripts is free software: you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as published by the
# Free Software Foundation, either version 3 of the License, or (at your
# option) any later version.
#
# xidian-scripts is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
# for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with xidian-scripts.  If not, see <http://www.gnu.org/licenses/>.

import pkg_resources
import subprocess
import sys

try:
    pkg_resources.require(('libxduauth'))
except (pkg_resources.DistributionNotFound, pkg_resources.VersionConflict):
    subprocess.check_call([
        sys.executable, '-m', 'pip', 'install', 'libxduauth'
    ])


import json
import sys
from libxduauth import EhallSession

USERNAME = sys.argv[1]
PASSWORD = sys.argv[2]
ses = EhallSession(USERNAME, PASSWORD)
ses.use_app(4768574631264620)

querySetting = [
    {  # 是否有效
        'name': 'SFYX',
        'value': '1',
        'linkOpt': 'and',
        'builder': 'm_value_equal'
    }
]
scoreList = []
for i in ses.post(
        'http://ehall.xidian.edu.cn/jwapp/sys/cjcx/modules/cjcx/xscjcx.do',
        data={
            'querySetting': json.dumps(querySetting),
            '*order': '+XNXQDM,KCH,KXH',
            # 请参考 https://github.com/xdlinux/xidian-scripts/wiki/EMAP#高级查询的格式
            'pageSize': 1000,
            'pageNumber': 1
        }
).json()['datas']['xscjcx']['rows']:
    # roman_nums = str.maketrans({
    #     'Ⅰ': 'I', 'Ⅱ': 'II', 'Ⅲ': 'III', 'Ⅳ': 'IV', 'Ⅴ': 'V', 'Ⅵ': 'VI', 'Ⅶ': 'VII', 'Ⅷ': 'VIII',
    # })  # 一些终端无法正确打印罗马数字
    # i["XSKCM"] = i["XSKCM"].translate(roman_nums)
    score = {}
    score['KCH']=i['KCH']#课程号
    score['KSSJ']=i['KSSJ']#考试时间
    score['XNXQDM']=i['XNXQDM']#学年学期
    score['KCXZDM_DISPLAY']=i['KCXZDM_DISPLAY']#修读类型
    score['KCLBDM_DISPLAY']=i['KCLBDM_DISPLAY']#课组名
    score['XSKCM']=i['XSKCM']#课程名
    score['ZCJ']=i['ZCJ']#总成绩
    score['XF']=i['XF']#学分
    score['SFJG']=i['SFJG']#是否及格
    score['KSLXDM_DISPLAY']=i['KSLXDM_DISPLAY']#考核方式
    scoreList.append(score)
print(json.dumps(scoreList,ensure_ascii=False))
